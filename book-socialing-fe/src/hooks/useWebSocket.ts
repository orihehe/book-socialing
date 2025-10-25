import { useCallback, useEffect, useRef, useState } from 'react'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

import type { ChatMessageResponse } from '@/types/chat'

interface UseWebSocketProps {
  token?: string
  noteId?: number
  onMessage?: (message: ChatMessageResponse) => void
  onConnect?: () => void
  onDisconnect?: () => void
  onError?: (error: unknown) => void
}

export function useWebSocket({ token, noteId, onMessage, onConnect, onError }: UseWebSocketProps) {
  const [isConnected, setIsConnected] = useState(false)
  const [isConnecting, setIsConnecting] = useState(false)
  const stompClientRef = useRef<Stomp.Client | null>(null)
  const socketRef = useRef<WebSocket | null>(null)

  const connect = useCallback(() => {
    if (isConnected || isConnecting) return

    setIsConnecting(true)

    try {
      // SockJS 연결 생성 - 토큰을 쿼리 파라미터로 전달
      const wsUrl = token ? `/ws?token=${encodeURIComponent(token)}` : '/ws'
      console.log('Attempting WebSocket connection to:', wsUrl)
      const socket = new SockJS(wsUrl)
      socketRef.current = socket

      // STOMP 클라이언트 생성
      const stompClient = Stomp.over(socket)
      // Disable debug logs to reduce spam
      stompClient.debug = () => {}

      stompClientRef.current = stompClient

      // JWT 토큰을 헤더에도 포함하여 연결
      const headers = token ? { Authorization: `Bearer ${token}` } : {}
      stompClient.connect(
        headers,
        (frame: unknown) => {
          console.log('✅ WebSocket Connected:', frame)
          setIsConnected(true)
          setIsConnecting(false)
          onConnect?.()

          // noteId 기반 채팅방 구독
          if (noteId) {
            stompClient.subscribe(`/topic/chat/${noteId}`, (message: Stomp.Message) => {
              try {
                const chatMessage: ChatMessageResponse = JSON.parse(message.body)
                console.log('Received message:', chatMessage)
                onMessage?.(chatMessage)
              } catch (error) {
                console.error('Error parsing message:', error)
              }
            })
          }
        },
        (error: unknown) => {
          console.error('❌ STOMP Connection Error:', error)
          setIsConnected(false)
          setIsConnecting(false)
          onError?.(error)
        }
      )
    } catch (error) {
      console.error('❌ WebSocket Connection Error:', error)
      setIsConnecting(false)
      onError?.(error)
    }
  }, [token, noteId, onMessage, onConnect, onError, isConnected, isConnecting])

  const disconnect = useCallback(() => {
    if (stompClientRef.current && isConnected) {
      stompClientRef.current.disconnect(() => {
        console.log('Disconnected')
      })
    }
    if (socketRef.current) {
      socketRef.current.close()
    }
    setIsConnected(false)
    setIsConnecting(false)
  }, [isConnected])

  const sendMessage = useCallback(
    (content: string, type: string = 'GENERAL') => {
      if (!stompClientRef.current || !isConnected || !noteId) {
        console.error('Not connected to WebSocket or noteId is missing')
        return
      }

      const message = {
        content,
        type,
        emojis: [],
      }

      stompClientRef.current.send(`/app/chat/${noteId}/sendMessage`, {}, JSON.stringify(message))
    },
    [isConnected, noteId]
  )

  // 컴포넌트 언마운트 시 연결 해제
  useEffect(() => {
    return () => {
      try {
        if (stompClientRef.current && stompClientRef.current.connected) {
          stompClientRef.current.disconnect(() => {
            console.log('Disconnected on unmount')
          })
        }
        if (socketRef.current) {
          socketRef.current.close()
        }
      } catch (error) {
        console.log('Error during cleanup:', error)
      }
    }
  }, [])

  return {
    isConnected,
    isConnecting,
    connect,
    disconnect,
    sendMessage,
  }
}
