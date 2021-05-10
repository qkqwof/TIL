## 문자열
인덱싱과 슬라이싱
split(), join()

``` python
mystr = "20210510Cloudy"

#인덱스와 슬라이싱을 이용해서 날짜와 날씨를 각각 할당하세요
mydate = mystr[0:8]

weather = mystr[8:]

mydate2 = mystr.split("C")[0]
all = mystr[:]


print(mydate2)
print(mydate)
print(weather)
print(all)
```

20210510
20210510
Cloudy
20210510Cloudy

``` python
movies = "미나리, 경계선, 기생충, 매트릭스"
print(movies)
moviesL = movies.split(",")
print(moviesL)

# join()은 str에 적용되는 함수
# 구분자.join(리스트)
# movie = ""
movie = "".join(moviesL)
print(movie)
```

#### 리스트 생성, 추가, 삭제, 수정

#### 1. 리스트 생성하기

``` python
mylist1 = [2,5,3,10,4]
mylist2 = []
mylist3 = list()

print(mylist1)
print(mylist2)
print(mylist3)

len(mylist2)

mylist3.extend(mylist1)
mylist3
```

[2, 5, 3, 10, 4]
[]
[]
[2, 5, 3, 10, 4]

#### 2. 리스트 수정하기

``` python
mylist1[3] = -10
mylist1 # 값 변경 가능
```

[2, 5, 3, -10, 4]

#### 3. 리스트 추가하기
- append()
- insert()

``` python
mylist1.append(100)
mylist1
mylist1.insert(3,200)
mylist1

# 리스트 가장 마지막에 -200을 추가...append 사용하지 말고 
mylist1.insert(len(mylist1),-200) # 마지막 인덱스가 len()-1
mylist1
```

[2, 5, 3, -10, 4, 100]
[2, 5, 3, 200, -10, 4, 100]

[2, 5, 3, 200, -10, 4, 100, -200]

#### 4. 리스트 삭제하기
del : 인덱스로 삭제
remove : 삭제할 값을 직접 입력
pop : 맨 마지막 값을 삭제한 후 리턴함

``` python
mylist = [1,2,3,4,4,5]
del mylist[2]
mylist

del mylist[0:2]
mylist

mylist.remove(4)
mylist

a = mylist.pop()
a
```

[1, 2, 4, 4, 5]
[4, 4, 5]
[4, 5]
5

#### 5. 리스트에서 값 추출하기 | 정렬하기 sort()

``` python
myL = [1,2,3,4,5,6,7,8]
print(myL[1:3]) #[2,3]
print(myL[:3]) #[1,2,3]
print(myL[4:]) #[5,6,7,8]
print(myL[-3:]) #[6,7,8]

myL.sort(reverse=True)
myL
```

[2, 3]
[1, 2, 3]
[5, 6, 7, 8]
[6, 7, 8]
[8, 7, 6, 5, 4, 3, 2, 1]

* 파이썬에서 모든 변수는 객체이다.
who 옵션을 사용하면 지금까지 사용한 객체 변수명을 출력
동시에 간단한 정보도 확인

%who
%whos - 변수명 뿐만 아니라 변수값도 함께 출력

``` python
%whos
```


Variable   Type    Data/Info

----------------------------
a          list    n=6
all        str     20210510Cloudy
b          list    n=3
movie      str     미나리 경계선 기생충 매트릭스
movies     str     미나리, 경계선, 기생충, 매트릭스
moviesL    list    n=4
myL        list    n=8
mydate     str     20210510
mydate2    str     20210510
mylist     list    n=1
mylist1    list    n=8
mylist2    list    n=0
mylist3    list    n=5
mystr      str     20210510Cloudy
weather    str     Cloudy

------

* list() 내장함수와 
list 전역변수의 이름이 동일하게 사용된 경우

이렇게 동일한 이름의 변수 선언시
메모리에서 찾는 순서
1) 로컬변수는 로컬영역에 저장 --- 여기를 가장 먼저 뒤진다.
2) 전역변수는 전역영역에 저장 --- 여기를 2번째로 찾는다.
3) 내장변수는 내장변수영역에 저장 --- 여기를 가장 마지막으로 찾는다.

-->
아래에서 전역변수 부분을 먼저 찾아서 돌려놓고
그런 다음에 내장변수를 찾았더니...이미 위에서 사용된 이름이라서
내장변수에서 에러가 난다.

Restart를 해서 메모리에서 싹 지워버리고
새롭게 다른 이름으로 할당

``` python
aList = list("abcd")
aList

list_data = [3,4,5]
print(list_data)
list_data.sort(reverse=True)
list_data

print(5 in list_data)
```

[3, 4, 5]
True

#### 6. 2차원 배열에서 데이터 추출하기

```python
test2D = [[1,3],[4,6]]
print(test2D)

print(test2D[1][1])

myLang = [['hello','python'],['good-bye','c']]
myLang

# 위 2차원 배열에서 good만 뽑아서 출력해보세요.
print(myLang[1][0].split("-")[0])
```

    [[1, 3], [4, 6]]
    6
    [['hello', 'python'], ['good-bye', 'c']]
    good