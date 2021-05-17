### BeautifulSoup

BeautifulSoup은 HTML과 XML문서를 파싱하기 위한 파이썬 라이브러리
    
BeautifulSoup parser를 사용해서 html 문서 내 태그를 구조화하고 파이썬 객체로 만들어서 원하는 태그를 찾을 수 있도록 도와준다.
    
BeautifulSoup 라이브러리는 외부라이브러리 이기 때문에 사용하기 위해서는 설치부터 해야 한다.(아나콘다는 이미 설치되어 있음) - pip install bs4
    
BeautifulSoup 인터넷 홈페이지 내용을 가지고 오는 모듈이 아니라 홈페이지 내용을 구조화해서 가져오는 모듈

### 1. 네이버 URL 정보를 가져옴 - requests.get()

### 2. BeautifulSoup을 이용해서 파싱함

``` python
import requests
from bs4 import BeautifulSoup

req = requests.get("https://naver.com")#네이버 url 정보를 가져온다.
html = req.text
soup = BeautifulSoup(html,"html.parser")# BeautifulSoup을 이용해서 html.parser로 파싱한다.
```

### 3. BeautifulSoup find() => 단수 , find_all() => 복수

``` python
result = soup.find_all("a","thumb")# a태그와 특정 클래스 등을 참조한다.

news_list=[]
for i in result:
    news_list.append(i.find("img")["alt"])# img태그 안에 있는 alt 속성값 추가...
print(news_list)
```

['오마이뉴스', '세계일보', '경향신문', '뉴시스', '머니투데이', 'SBS', '스포츠서울', '프레시안', '중앙일보', '아이뉴스24', '스포츠조선', 'JTBC', '전자신문', '연합뉴스TV', '조선일보', '스타뉴스', '미디어SR', '이투데이', '여성신문', '포브스코리아', '투데이신문', '이코노믹리뷰', '게임메카', 'SBS연예뉴스']

### BeautifulSoup이 가지고 있는 특정 함수들...

``` python
soup.title
soup.title.name
soup.title.string
soup.title.get_text()
```

< title >NAVER</ title >
'title'
'NAVER'
'NAVER'

#### 태그가 여러개 있더라도 항상 첫번쨰 태그만 가져와서 검색한다...주의!!

``` python
soup.img
```

< img alt="" height="49" src="https://static-whale.pstatic.net/main/img_whaleon_v2@2x.png" style="padding-left: 65px" width="290"/>

``` python
# 해당 태그의 속성을 가져올 떄
soup.img["alt"]

#속성이 존재하지 않으면 에러...
soup.img["class"]
```

''

``` python
soup.img["height"]
import re #정규식 표현관련 모듈
# 특정단어가 얼마나 들어있는지 확인
print(soup.find_all(string=re.compile("네이버")))
```

'49'
['네이버', '네이버를 시작페이지로', '쥬니어네이버', '네이버뉴스', '언론사가 직접 편집한 뉴스들을 네이버 홈에서 바로 보실 수 있습니다.', '네이버 검색어 제안 기능이 업데이트 됩니다.', '네이버 개발자 센터', '네이버 D2', '네이버 D2SF', '네이버 랩스', '네이버 정책 및 약관', '네이버 정책']

### BeautifulSoup의 select_one(), select()
find(), find_all() ---- 태그 기반 검색
select_one(), select() ---- 선택자 기반 검색

``` python
print(soup.select_one('a')) # a 태그 하나만 가져옴
print(soup.select("a")) # a 태그 다 가져옴
```

``` python
# css 선택자 사용하는 기법 그래도 사용한다.
body = soup.select("body a")
#body

ul = soup.select("div>ul")
ul
```

둘다 가져오는 결과가 길어서 결과는 생략한다.

### BeautifulSoup get_text(), get()
get_text() ---- 검색결과에서 태그를 제외한 텍스트만 가져옴
get("속성") ---- 해당 속성의 값만 가져옴
string() ---- 검색결과에서 태그 안의 또다른 태그가 없는 경우에만 내용 출력

``` python
text = soup.find("span",attrs={"class":"blind"})
text.get_text()
text.get("class")
```
'NAVER whale'
['blind']

### BeautifulSoup 사용해서 네이버 랭킹 가져오기
1. 네이버 영화랭킹 홈페이지 접속 url, text 가져오기
https://movie.naver.com/movie/sdb/rank/rmovie.nhn
2. 분석잡업을 하기 위해서 BeautifulSoup을 만들고 html 파서로 파싱
3. 리턴된 soup 객체를 분석한 결과로 영화랭킹을 찾아서 출력
select(),find_all()

``` python
import requests
from bs4 import BeautifulSoup

# 1. 특정 사이트 url 받아와서...requests.get("")
req = requests.get("https://movie.naver.com/movie/sdb/rank/rmovie.nhn")
html = req.text

# 2. 분석작업을 하기 위해서 bs 생성
soup = BeautifulSoup(html,"html.parser")

# 3. soup 객체를 분석해서 영화랭킹을 찾아본다.(영화랭킹 태그가 어떤 구조로 되어있는지....)
'''
영화랭킹 태그 구조는 
div 태그의 클래스 속성의 값이 tit3
    a태그들이 계속 반복되는 구조...
'''

movie = soup.find_all("div","tit3")
for i in movie:
    print("{:2}위 :{}".format(i+1,movie[i].get_text().strip()))
```

1위 :스파이럴
 2위 :더 스파이
 3위 :분노의 질주: 더 얼티메이트
 4위 :크루즈 패밀리: 뉴 에이지
 5위 :비와 당신의 이야기
 6위 :극장판 귀멸의 칼날: 무한열차편
 7위 :내가 죽기를 바라는 자들
 8위 :아들의 이름으로
 9위 :내일의 기억
10위 :미나리
11위 :내겐 너무 소중한 너
12위 :노매드랜드
13위 :보이저스
14위 :명탐정 코난: 비색의 탄환
15위 :자산어보
16위 :링: 더 라스트 챕터
17위 :아이들은 즐겁다
18위 :키퍼스
19위 :어른들은 몰라요
20위 :혼자 사는 사람들
21위 :극장판 콩순이: 장난감나라 대모험
22위 :슈퍼노바
23위 :해리 포터와 아즈카반의 죄수
24위 :도라에몽: 스탠바이미 2
25위 :범털2 : 쩐의 전쟁
26위 :빅 피쉬
27위 :크루엘라
28위 :노바디
29위 :학교 가는 길
30위 :미스
31위 :파이터
32위 :비커밍 아스트리드
33위 :파이프라인
34위 :낙원의 밤
35위 :베놈 2: 렛 데어 비 카니지
36위 :소울
37위 :콰이어트 플레이스 2
38위 :8년을 뛰어넘은 신부
39위 :더 파더
40위 :그린 북
41위 :카오스 워킹
42위 :2067
43위 :잭 스나이더의 저스티스 리그
44위 :블랙 위도우
45위 :500일의 썸머
46위 :모탈 컴뱃
47위 :컨저링 3: 악마가 시켰다
48위 :스쿨 오브 락(樂)
49위 :서복
50위 :고질라 VS. 콩

