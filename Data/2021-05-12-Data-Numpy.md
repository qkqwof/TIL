## Numpy
Numeric Python의 약자
    수학분야 관련 통계 연산 작업시 사용
    과학 계산 컴퓨팅, 데이터 분석에 필요한 기본적인 패키지
    
Numpy배열은 리스트와 거의 흡사
    리스트보다 훨씬 빠르고 메모리 효율성이 높아서 성능상 우위를 차지한다.
    
Numpy는 사용에 앞서서 import해서 사용해야 한다.
    Numpy 배열 만드는 방법
    1) array() 사용
    2) random 모듈의 랜덤함수 사용

``` python
import numpy as np
```

### np 배열 생성 
#### 리스트와 비교
    array() 함수 사용

``` python
myList = [4,5,6,7]
myArr = np.array(myList)

print(myList)
print(myArr)

print(type(myList))
print(type(myArr))
```

[4, 5, 6, 7]
[4 5 6 7]
<class 'list'>
<class 'numpy.ndarray'>

ndarray는 출력 결과 [ ] 안에 값들이 나열되지만 ,로 구분되진 않는다.
ndarray타입
ndarray 클래스는 다차원 행렬구조를 지원하는 Numpy의 핵심 클래스

``` python
myList_sub = myList[1:3]
print(myList_sub)

myArr_sub = myArr[1:3]
print(myArr_sub)

myList_sub[0] = -5
myArr_sub[0] = -5
print(myList_sub)
print(myArr_sub)
```

[5, 6]
[5 6]
[-5, 6]
[-5  6]

``` python
print(myList) # list => 원본이 안 바뀐다.
print(myArr) # np배열 => 원본이 바뀐다.
```

[4, 5, 6, 7]
[ 4 -5  6  7]

리스트는 원본이 안 바뀐다. 변경되면 기존의 것을 카피해놓고 새로운 것을 만든다.
반면에 np배열은 원본뷰가 바뀌어버린다.
이것이 리스트와 np배열의 가장 큰 차이점이다.
메모리 효율성과 연관되는 부분이다.
<br>

#### np.random 사용하기
**rand() 함수 사용하기**

``` python
a = np.random.rand(5)
print(a)
print(type(a))
```

[0.88049062 0.11804139 0.23027837 0.38924963 0.64961181]
<class 'numpy.ndarray'>

**randint() 함수 사용하기**

``` python
b = np.random.randint(1,10,5)
print(b)
print(type(b))
```

[4 2 1 6 1]
<class 'numpy.ndarray'>

**randn() 함수 사용하기**
가우시안 표준분포에 해당하는 값을 뽑아냄

``` python
c = np.random.randn(10)
print(c)
print(type(c))
```

[-0.90998277 -0.06878845  1.72072705  1.12136733 -0.89303558 -1.38616033
  2.05390119  0.15333402  0.86693588  0.31979775]
<class 'numpy.ndarray'>

### np배열의 초기화
zeros(), ones(), arange()...함수를 이용해서 배열 생성과 동시에 특정한 값으로
초기화할 수 있다.

``` python
# 1.
az = np.zeros(10)
print(az)

#2. 1값으로 이뤄진 크기가 10인 배열 생성
ao = np.ones(10)
print(ao)

#3. 3 by 3 단위의 배열 생성(대각선에는 1)
ae = np.eye(3)
print(ae)

#4. arange()
print(np.arange(3))
print(np.arange(3,7))
print(np.arange(3,10,2))

print("*"*30)

a7 = np.zeros(10)+7
print(a7)

af = np.full((5,5),-11)
print(af)

af = np.full((7,5),23)
print(af)
```

[0. 0. 0. 0. 0. 0. 0. 0. 0. 0.]
[1. 1. 1. 1. 1. 1. 1. 1. 1. 1.]
[[1. 0. 0.]
[0. 1. 0.]
[0. 0. 1.]]
[0 1 2]
[3 4 5 6]
[3 5 7 9]

******************************

[7. 7. 7. 7. 7. 7. 7. 7. 7. 7.]
[ [-11 -11 -11 -11 -11]
 [-11 -11 -11 -11 -11]
 [-11 -11 -11 -11 -11]
 [-11 -11 -11 -11 -11]
 [-11 -11 -11 -11 -11]]
[ [23 23 23 23 23]
 [23 23 23 23 23]
 [23 23 23 23 23]
 [23 23 23 23 23]
 [23 23 23 23 23]
 [23 23 23 23 23]
 [23 23 23 23 23]]
<br>

#### np배열과 리스트의 차이점
1. np배열은 원본을 바로 바꿈으로써 메모리 효율성이 뛰어나다
2. 리스트는 여러가지 자료형이 원소로 올 수 있지만
np배열은 한가지 자료형만 원소로 쓰일 수 있다.

``` python
# 리스트는 자료형 구조로 서로 다른 자료형이 원소로 올 수 있다.
L = [1,2,3,'4',4,'하바리']
print(L)
print(type(L))

# np.array는 하나의 자료형만 저장 가능
A = np.array(L)
print(A)
print(type(A))
# capa가 더 큰 자료형으로 변환
A2 = np.array([1.6,3,8,3])
print(A2)

A3 = np.array([1,2,3,4], dtype="float")
print(A3)

A4 = np.array([1,2,3,4], dtype=np.float64)
print(A4)
```

[1, 2, 3, '4', 4, '하바리']
<class 'list'>
['1' '2' '3' '4' '4' '하바리']
<class 'numpy.ndarray'>
[1.6 3.  8.  3. ]
[1. 2. 3. 4.]
<br>

#### Numpy 배열에서 사용되는 함수, 속성
ndim shape reshape() size type dtype astype()

``` python
arr1 = np.array([[1.0, 2,3],[4,5,6]], dtype = np.int32) # 속성인 dtype을 정수로 지정해주기
arr1
```

array([ [1, 2, 3],
       [4, 5, 6]])

``` python
# 기능
print(type(arr1))
print(np.ndim(arr1)) # 매트릭스
print(np.shape(arr1)) # 몇행 몇열
print(np.size(arr1)) # 개수

# 속성
print("*"*30)
print(arr1.ndim)
print(arr1.shape)
print(arr1.size)
print(arr1.dtype)

print("*"*30)
# ndarray 안에 들어있는 모든 요소들의 타입을 한방에 바꿔버리는 기능...astype

arr1_1 = arr1.astype(np.float32)
print(arr1_1)

print("*"*30)
arr2 = np.arange(32)
print(arr2)

arr2_1 = np.arange(32).reshape(4,8)
print(arr2_1)
```

<class 'numpy.ndarray'>
2
(2, 3)
6
******************************
2
(2, 3)
6
int32
******************************
[ [1. 2. 3.]
 [4. 5. 6.]]
******************************
[ 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
 24 25 26 27 28 29 30 31]
[ [ 0  1  2  3  4  5  6  7]
 [ 8  9 10 11 12 13 14 15]
 [16 17 18 19 20 21 22 23]
 [24 25 26 27 28 29 30 31]]

 <br>

 ### 랜덤함수와 seed 값 설정하기
seed값은 한번만 설정해주면 계속 적용된다.
시드는 보통 현재시각 등을 이용해서 자동으로 정해지지만
우리가 수동으로도 설정할 수 있다.
특정한 시드값이 사용되면 그 다음에 만들어지는 난수들을 예측할 수 있다.

``` python
import numpy as np
np.random.seed(0) # 수동...(0포함 양의 정수)

arr1 = np.random.randn(4,4)
print(arr1)
```

[ [ 1.76405235  0.40015721  0.97873798  2.2408932 ]
 [ 1.86755799 -0.97727788  0.95008842 -0.15135721]
 [-0.10321885  0.4105985   0.14404357  1.45427351]
 [ 0.76103773  0.12167502  0.44386323  0.33367433] ]

``` python
arr2 = np.arange(1,10)
arr2

arr3 = np.arange(1,10).reshape(3,3)
arr3

```

array([ [1, 2, 3],
       [4, 5, 6],
       [7, 8, 9] ])

#### 인덱싱, 슬라이싱

``` python
narray = np.arange(5)
narray

# 1. 인덱싱, 슬라이싱으로 값 추출하기
print(narray[1], narray[4])
print(narray[1:5])
print(narray[:])
print(narray[2:4])
```

array([0, 1, 2, 3, 4])
1 4
[1 2 3 4]
[0 1 2 3 4]
[2 3]

``` python
narray2 = np.random.randint(10,20,16).reshape(4,4)
narray2
```

array([ [12, 17, 12, 10],
       [10, 14, 15, 15],
       [16, 18, 14, 11],
       [14, 19, 18, 11] ])

``` python 
# 1) narray2 중에서 첫번쨰 행의 세번째 값 12를 가져오려면?
narray2[0][2]
narray2[0,2]

# 2) 2번째 라인 [10,14,15,15] 전체를 출력
narray2[1:2,0:4]
#narray2[1:2,:]
#narray2[1,:]
narray2[1,0:]

# 3) 전체 행에 대해서 4번째 열을 출력?
narray2[0:4, 3:4]
narray2[:, 3:]
narray2[:,3]

# 4) 전체 행에 대해서 1,2번째까지의 열을 출력
narray2[:,:2]
```

#### 인덱싱과 조건 슬라이싱

``` python
arr1
# 1. 논리연산자 사용해서 값을 받아오거나 값을 변경

arr1[arr1>0]
arr1[arr1<0]
arr1[arr1<0] = 0
# 원본도 함꼐 바뀐다...
arr1
```

array([ [1.76405235, 0.40015721, 0.97873798, 2.2408932 ],
       [1.86755799, 0.        , 0.95008842, 0.        ],
       [0.        , 0.4105985 , 0.14404357, 1.45427351],
       [0.76103773, 0.12167502, 0.44386323, 0.33367433] ])

``` python
# 2. where 함수
arr1_1 = np.where(arr1>0,arr1,-1)
arr1_1
```

array([ [ 1.76405235,  0.40015721,  0.97873798,  2.2408932 ],
       [ 1.86755799, -1.        ,  0.95008842, -1.        ],
       [-1.        ,  0.4105985 ,  0.14404357,  1.45427351],
       [ 0.76103773,  0.12167502,  0.44386323,  0.33367433] ])

``` python
# 3. 1,2를 가지고 응용하기
arr2 = np.array([[1,2],[3,4],[5,6]])
arr2

# 논리연산자 2보다 큰 수만 출력 되도록
bool_idx = arr2>2
print(arr2[bool_idx])
```

array([ [1, 2],
       [3, 4],
       [5, 6] ])

[3 4 5 6]

