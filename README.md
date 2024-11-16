ERP 시스템 데이터베이스 설계 문서
1. 사용자 및 권한 관리 모듈
   1.1 사용자 정보 테이블 (users)
   컬럼명	데이터 타입	NULL 허용	설명
   user_id	VARCHAR(50)	NOT NULL	사용자 고유 ID (Primary Key)
   username	VARCHAR(100)	NOT NULL	사용자 이름
   password	VARCHAR(255)	NOT NULL	암호화된 비밀번호
   email	VARCHAR(100)	NOT NULL	사용자 이메일 주소
   role_id	VARCHAR(50)	NOT NULL	사용자 권한 ID (Foreign Key)
   created_at	TIMESTAMP	NOT NULL	계정 생성 일시
   updated_at	TIMESTAMP	NULL	계정 수정 일시
   1.2 역할 및 권한 테이블 (roles)
   컬럼명	데이터 타입	NULL 허용	설명
   role_id	VARCHAR(50)	NOT NULL	권한 ID (Primary Key)
   role_name	VARCHAR(100)	NOT NULL	권한 이름 (예: 관리자, 사용자 등)
   description	VARCHAR(255)	NULL	권한 설명
2. 재고 및 물류 관리 모듈
   2.1 상품 테이블 (products)
   컬럼명	데이터 타입	NULL 허용	설명
   product_id	VARCHAR(50)	NOT NULL	상품 고유 ID (Primary Key)
   product_name	VARCHAR(100)	NOT NULL	상품명
   category_id	VARCHAR(50)	NOT NULL	카테고리 ID (Foreign Key)
   price	DECIMAL(10,2)	NOT NULL	상품 단가
   stock_quantity	INT	NOT NULL	재고 수량
   created_at	TIMESTAMP	NOT NULL	상품 생성 일시
   updated_at	TIMESTAMP	NULL	상품 수정 일시
   2.2 재고 입출고 기록 테이블 (inventory_transactions)
   컬럼명	데이터 타입	NULL 허용	설명
   transaction_id	VARCHAR(50)	NOT NULL	거래 고유 ID (Primary Key)
   product_id	VARCHAR(50)	NOT NULL	상품 ID (Foreign Key)
   transaction_type	VARCHAR(20)	NOT NULL	거래 유형 (입고, 출고)
   quantity	INT	NOT NULL	거래 수량
   transaction_date	TIMESTAMP	NOT NULL	거래 일시
   warehouse_id	VARCHAR(50)	NOT NULL	창고 ID (Foreign Key)
3. 생산 관리 모듈
   3.1 작업 지시 테이블 (production_orders)
   컬럼명	데이터 타입	NULL 허용	설명
   order_id	VARCHAR(50)	NOT NULL	작업 지시 고유 ID (Primary Key)
   product_id	VARCHAR(50)	NOT NULL	상품 ID (Foreign Key)
   quantity	INT	NOT NULL	생산 예정 수량
   start_date	DATE	NOT NULL	생산 시작일
   end_date	DATE	NULL	생산 종료일
   status	VARCHAR(20)	NOT NULL	작업 상태 (진행 중, 완료 등)
   3.2 생산 기록 테이블 (production_records)
   컬럼명	데이터 타입	NULL 허용	설명
   record_id	VARCHAR(50)	NOT NULL	생산 기록 고유 ID (Primary Key)
   order_id	VARCHAR(50)	NOT NULL	작업 지시 ID (Foreign Key)
   actual_quantity	INT	NOT NULL	실제 생산된 수량
   production_date	DATE	NOT NULL	생산 완료 일자
4. 인사 관리 모듈
   4.1 직원 정보 테이블 (employees)
   컬럼명	데이터 타입	NULL 허용	설명
   employee_id	VARCHAR(50)	NOT NULL	직원 고유 ID (Primary Key)
   name	VARCHAR(100)	NOT NULL	직원 이름
   department_id	VARCHAR(50)	NOT NULL	부서 ID (Foreign Key)
   position	VARCHAR(50)	NOT NULL	직급
   salary	DECIMAL(10,2)	NOT NULL	급여
   hire_date	DATE	NOT NULL	입사일
   4.2 급여 기록 테이블 (payroll)
   컬럼명	데이터 타입	NULL 허용	설명
   payroll_id	VARCHAR(50)	NOT NULL	급여 기록 고유 ID (Primary Key)
   employee_id	VARCHAR(50)	NOT NULL	직원 ID (Foreign Key)
   pay_period	DATE	NOT NULL	급여 지급 기간
   amount	DECIMAL(10,2)	NOT NULL	지급 금액
5. 회계 관리 모듈
   5.1 거래 내역 테이블 (transactions)
   컬럼명	데이터 타입	NULL 허용	설명
   transaction_id	VARCHAR(50)	NOT NULL	거래 고유 ID (Primary Key)
   account_id	VARCHAR(50)	NOT NULL	계정 ID (Foreign Key)
   amount	DECIMAL(10,2)	NOT NULL	거래 금액
   transaction_date	TIMESTAMP	NOT NULL	거래 일시
   description	VARCHAR(255)	NULL	거래 설명
6. 구매 및 공급망 관리 모듈
   6.1 구매 요청 테이블 (purchase_orders)
   컬럼명	데이터 타입	NULL 허용	설명
   purchase_id	VARCHAR(50)	NOT NULL	구매 요청 ID (Primary Key)
   supplier_id	VARCHAR(50)	NOT NULL	공급업체 ID (Foreign Key)
   product_id	VARCHAR(50)	NOT NULL	상품 ID (Foreign Key)
   quantity	INT	NOT NULL	주문 수량
   order_date	DATE	NOT NULL	주문일
   status	VARCHAR(20)	NOT NULL	상태 (주문, 발주 완료 등)
7. 데이터베이스 설계 시 고려 사항
   정규화: 데이터 중복을 방지하고 데이터 무결성을 보장하기 위해 3차 정규화 적용.
   인덱싱: 자주 조회하는 컬럼(예: user_id, product_id)에 인덱스 추가.
   보안: 사용자 데이터 보호를 위해 비밀번호 암호화 및 접근 제어 적용.
   백업 및 복구: 스프링 배치 작업을 활용한 정기적인 데이터 백업 및 복구 절차 구축.