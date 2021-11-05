*   Room 라이브러리

    스마트폰 내장 DB에 데이터를 저장하기 위해 사용하는 라이브러리
    SQLite를 활용하여 객체 매핑을 도와주는 역할.

    Room = (Room DataBase + Data Access Objects + Entity)

    Entity(개체)  : 대상에 대한 속성 (논리 DB에서의 개체)               (data class)
    Object(객체)  : 대상에 대한 정보(속성), 동작, 기능, 절차 포함.       (class)
    DAO          : 데이터에 접근할 수 있는 메소드를 정의해놓은 인터페이스. (interface)
    Room DB      : DB를 생성, 관리하는 상속 객체                      (abstract class)

    -> Room Library 를 사용하기 위해서는
    1. Entity 생성
    2. DAO 생성
    3. RoomDataBase 상속
    4. 데이터 베이스 사용 (싱글톤)
    5. 데이터 베이스 사용시 실행 방식 (컴퓨팅 자원 강제 실행 / 코루틴 비동기)