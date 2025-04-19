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
