### ⚠️ Caution: After cloning the repository, run installGitHooks to protect the main branch! 🔒

## 🛠️ BE Environment
### VM Options
```
-Dspring.profiles.active=dev
-Dspring.auth.active=false // 인증 없이 개발을 원한다면
```

### Swagger API 문서

개발 환경에서 실행 중인 애플리케이션의 Swagger API 문서는 다음 경로에서 확인하실 수 있습니다:

[Swagger UI 바로가기](http://localhost:8080/swagger-ui/index.html)

**참고:** `localhost:8080`은 로컬 개발 환경의 기본 주소입니다. 실제 배포 환경에서는 해당 서버의 도메인과 포트 번호로 변경됩니다. (예: `https://api.your-service.com/swagger-ui/index.html`)

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

---

### 📦 설치 방법
```bash
pnpm install
pnpm dev
