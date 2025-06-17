### ⚠️ Caution: After cloning the repository, run installGitHooks to protect the main branch! 🔒


1. 로컬에서 SSH 생성
- ssh-keygen -t rsa -b 4096 -C "test@gmail.com"
생성되는 키
공개 키: ~/.ssh/id_rsa.pub
개인 키: ~/.ssh/id_rsa (이걸로 SSH 접속함)

2. root 계정 관리자에게 id_rsa.pub (공개키) 전달

3. root 계정 관리자는 해당 유저 추가
sudo adduser test
sudo mkdir /home/test/.ssh
sudo nano /home/test/.ssh/authorized_keys
- id_rsa.pub  내용 붙여넣기

4. 권한 설정
sudo chown -R test:test/home/test/.ssh
sudo chmod 700 /home/test/.ssh
sudo chmod 600 /home/test/.ssh/authorized_keys


-----------------------------------
ssh로 서버 접근 방법
mobaxterm 설치
1. MobaXterm 실행 → 좌측 상단 "Session" 클릭 → "SSH" 선택
2. remote host 입력
- ec2-43-200-163-220.ap-northeast-2.compute.amazonaws.com
3. Specify username 활성화
- user 입력: test
4. 탭 하단에서 "Use private key" 체크
- 개인키 경로 설정 (~/.ssh/id_rsa)
5. save 및 실행

---

## 🛠️ FE Tech Stack

### 🧱 프로젝트 기반
- **[Vite](https://vitejs.dev/)** - 빠른 번들링과 개발 환경을 위한 빌드 도구
- **[React](https://react.dev/)** + **[TypeScript](https://www.typescriptlang.org/)** - 타입 안정성과 컴포넌트 기반 UI

### 🎨 UI & 스타일링
- **[TailwindCSS](https://tailwindcss.com/)** - 유틸리티 퍼스트 CSS 프레임워크
- **[shadcn/ui](https://ui.shadcn.com/)** - 접근성과 확장성이 뛰어난 UI 컴포넌트 라이브러리

### 🧠 상태 관리 & 서버 상태
- **[Zustand](https://zustand-demo.pmnd.rs/)** - 간단하고 직관적인 클라이언트 상태 관리
- **[React Query](https://tanstack.com/query/latest)** - 서버 상태 캐싱 및 비동기 처리

### 📋 폼 & 유효성 검사
- **[react-hook-form](https://react-hook-form.com/)** - 선언형 폼 상태 관리
- **[zod](https://zod.dev/)** - 타입 기반 스키마 유효성 검증

### 📖 문서화
- **[Storybook](https://storybook.js.org/)** – UI 컴포넌트 개발 및 상태별 문서화 도구  
  → `pnpm storybook`으로 실행 후 [http://localhost:6006](http://localhost:6006) 에서 확인 가능

---

### 📦 설치 방법
```bash
pnpm install
pnpm dev
