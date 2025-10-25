import path from 'path'

import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  define: {
    global: 'globalThis',
  },
  server: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        // target: 'http://localhost:8080',
        target: 'http://saisai-dev.duckdns.org',
        changeOrigin: true,
        secure: false,
      },
      '/ws': {
        target: 'http://saisai-dev.duckdns.org',
        changeOrigin: true,
        secure: false,
        ws: true,
      },
    },
  },
})
