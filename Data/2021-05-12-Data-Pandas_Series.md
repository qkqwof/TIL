## Pandas
Pandas는 Panel Datas의 약자
파이썬을 이용한 데이터 분석에서 가장 많이 사용되는 라이브러리
Numpy 기반으로 만들어졌다.
    
**Pandas 자료구조**
1. Series - 1차원 배열(벡터)
    
2. DataFrame - 2차원 배열(매트릭스) 가장 중요(표형식):엑셀
    
3. Panel - 3차원(대상 아님)

#### Series
    시리즈 생성, 구조확인

``` python
import numpy as np
import pandas as pd
from pandas import Series, DataFrame
import matplotlib.pyplot as plt

ser1 = Series(np.random.randint(10,20,5), index=list("abcde"))
ser1
print(ser1.index)
print(ser1.values)
print(ser1.dtype)
print(ser1)
```

Index(['a', 'b', 'c', 'd', 'e'], dtype='object')
[12 11 15 15 14]
int32
a    12
b    11
c    15
d    15
e    14
dtype: int32

index 속성없이 시리즈를 생성하면
인덱스는 자동적으로 0~n-1까지 정수로 잡힌다.

#### 시리즈 값 조회하기
단일값을 선택하거나 여러값을 선택할 때
1) 인덱스로 라벨을 사용할 수 있다.
    
2)슬라이싱 방법
- 라벨 사용 : 마지막 라벨 포함됨 ['a' : 'b']
- 숫자 사용 : 마지막 숫자 포함 안됨

``` python
ser1
```

a    12
b    11
c    15
d    15
e    14
dtype: int32

``` python
ser1[0]
ser1["a"]
# 슿라이싱에서 숫자를 사용 | 라벨을 사용하는 경우
print(ser1[1:4])
print(ser1["b":"d"])
```

b    11
c    15
d    15
dtype: int32
b    11
c    15
d    15
dtype: int32

``` python
# 응용해서 조회하기 : 아래 2개의 표현법을 구분하자
ser1[:2]
print(ser1[:2])
ser1_1 = ser1[::2]
print(ser1_1)
```

a    12
b    11
dtype: int32
a    12
c    15
e    14
dtype: int32

첫번째 print문은 인덱스 번호가 0번부터 1번까지인 값을 호출하는 것이다.
두번째 print문은 모든 인덱스 번호를 호출하는데 사이의 간격이 2인 것이다. 따라서 a,c,e가 호출된다는 것을 알 수 있다.

``` python
# 시리즈 간의 연산

# NaN -> 데이터 X, 결측값...누락데이터
# 시리즈 -> 1차원 배열(따라서 capa가 크면 그것에 따라 성질이 맞춰짐)
resSer = ser1 + ser1_1
print(resSer)
```

a    24.0
b     NaN
c    30.0
d     NaN
e    28.0
dtype: float64

#### 누락 데이터 조회하기
isnull() | notnull()

``` python
resSer
```

a    24.0
b     NaN
c    30.0
d     NaN
e    28.0
dtype: float64

``` python
print(resSer.isnull())
print(resSer[resSer.isnull()])
print("*"*30)
print(resSer.notnull())
print(resSer[resSer.notnull()])
print("*"*30)
print(resSer.isnull().sum())
```

a    False
b     True
c    False
d     True
e    False
dtype: bool
b   NaN
d   NaN
dtype: float64
******************************
a     True
b    False
c     True
d    False
e     True
dtype: bool
a    24.0
c    30.0
e    28.0
dtype: float64
******************************
2

* numpy에서 배웠던 것과 같이 resSer.isnull()은 bool연산자로 밖에 안나오지만 resSer에 대괄호 안에 resSer.isnull()을 넣어주면 NaN인 인덱스와 값이 호출된다.

### Pandas를 이용한 시각화
#### 누락데이터가 아닌 데이터를 한눈에 보여준다.

``` python
resSer[resSer.notnull()].plot(kind="bar")
#resSer[resSer.isnull()].plot(kind="bar")
plt.show()
```

![판다스시각화](./img/판다스시각화.png)