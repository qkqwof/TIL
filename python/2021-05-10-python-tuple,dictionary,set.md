### Tuple - 데이터 조작이 불가능 Immutable
튜플은 몇가지 점을 제외하고는 거의 리스트와 비슷하다

<다른점>
1. 리스트는 []값을 둘러싸지만 튜플은 ()를 사용한다.
2. 리스트는 값을 추가, 삭제, 수정...즉 변경이 가능하지만 
      튜플은 한번 생성된 값을 수정할 수 없다.
3. 만약에 튜플로 만들어진 값을 조작하고 싶다면
리스트로 형을 변환시킨 다음에 변경할 수 있다. list(t)

#### 1.튜플 생성하기

``` python
t1 = (1,2,3,4)
t2 = (4,5,6)
t1
t2

#중요
t3 = (1) # 값이 한개일 때...
t4 = (1,)
print(type(t3))
print(type(t4))
```

<class 'int'>
<class 'tuple'>

#### 튜플을 사용하는 경우
프로그램이 실행되는 동안 그 값이 할상 변경되지 않기를 바란다거나
값이 바뀔까봐 걱정하고 싶지 않다면 무조건 튜플을 사용
이와는 반대로 수시로 그 값을 변경시켜야 하는 경우라면 리스트를 필히 사용

``` python
a,b,c = 3,4,5
print(type(a))

a = 3,4,5 # () 생략됨
print(type(a))

t = (1,2,3,4,5,6,7,8,9,0,100)

tList = list(t)
tList
```

<class 'int'>
<class 'tuple'>
[1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 100]

<br>
len() 컨테이너에 포함된 데이터 항목의 갯수를 리턴

컨테이너란 튜플, 딕셔너리, 리스트, 셋

값들의 수정이 불필요한 경우라면, 리스트보다는 튜플을 이용하는 것이 효율적이다.
왜? 메모리 사용이 훨씬 적다.

sys.getsizeof() - 특정 개체의 메모리 사이즈를 바이트 단위로 반환
직접 메모리 용량을 손쉽게 확인 가능

``` python
import sys
print(len(t))
print(len(tList))
print("튜플",sys.getsizeof(t))
print("리스트",sys.getsizeof(tList))
```

11
11
튜플 128
리스트 144

<br>

튜플은 값의 삭제 및 변경을 할 수 없다.

<br>

### 딕셔너리
#### 1. 생성하기

```python
pro_Dic = {"name":"DRAM","capa":"32g","maker":"SK","price":320000}
pro_Dic
```

{'name': 'DRAM', 'capa': '32g', 'maker': 'SK', 'price': 320000}

#### 2. 데이터 조회하기
1) [key]  
2) get(key)

- 딕셔너리 데이터를 조회할 때는 리스트, 튜플과 달리 인덱싱, 슬라이싱 기법 적용 안된다.
- 딕셔너리는 단 한가지 방법으로 조회 가능...key를 통해서 value를 찾는다.

```python
print(pro_Dic['name'])
print(pro_Dic.get("name"))
```

DRAM
DRAM

#### 3. 데이터 수정하기

``` python
pro_Dic['price'] = 230000
pro_Dic

addInfo = {'loc':"이천","pop":33}
pro_Dic.update(addInfo)
pro_Dic
```

{'name': 'DRAM', 'capa': '32g', 'maker': 'SK', 'price': 230000}

{'name': 'DRAM',
 'capa': '32g',
 'maker': 'SK',
 'price': 230000,
 'loc': '이천',
 'pop': 33}

#### 4. 데이터 삭제하기
del pro_Dic[key]

- del pro_Dic[3]... 인덱스 접근이 안된다.

``` python
del pro_Dic["pop"]
pro_Dic
```

{'name': 'DRAM', 'capa': '32g', 'maker': 'SK', 'price': 230000, 'loc': '이천'}

### 딕셔너리를 다룰 떄 주의할 사항
 1) 키값은 중복될 수 없다. 만약에 키가 중복되면 마지막 값으로 덮어씌움
2) 키값으로는 리스트 사용 못함 -- 튜플은 가능
3) key - value -- 없는 key를 입력할 때 서로 다른 결과

``` python
#a = {[1,2,3]:"hi"}
#a

b = {(1,2,3):"hello"}
b
```

{(1, 2, 3): 'hello'}

<br>

#### 딕셔너리 함수 사용하기
keys() | values() | items()

``` python
# dic의 키값만 뽑아서 출력
dic_keys = dic.keys() 
print(dic_keys) # dic_keys 객체 리턴
print(type(dic_keys))

# 키들의 리스트를 리스트로 변환시켜서 출력
print(list(dic_keys))
'''
dic_keys 객체는 리스트를 사용하는 것과 별반 다르지 않지만
리스트 고유의 기능...append, insert, pop, remove...sort 함수를 적용할 수 없다.
필요하면 list로 변환 시켜놓고 함수 적용해야 한다.
'''

#값들의 목록을 받아보자
dic_values = dic.values()
print(dic_values)
print(type(dic_values)) #dict_values 객체로 반환

# key,value 모두 얻기
dic_items = dic.items()
print(dic_items)
print(type(dic_items))
```

dict_keys(['name', 'age', 'address', 'phone'])
<class 'dict_keys'>
['name', 'age', 'address', 'phone']
dict_values(['Peter', 34, 'Texas', '1201234'])
<class 'dict_values'>
dict_items([('name', 'Peter'), ('age', 34), ('address', 'Texas'), ('phone', '1201234')])
<class 'dict_items'>

``` python
dic

name1 = dic.get("aaa")
print(name1) # 존재하지 않는 키를 입력하면 None 리턴

#name2 = dic["bbb"]
#print(name2) # 존재하지 않는 키를 입력하면 에러 발생

print('bbb' in dic)
print('name' in dic)
```

None
False
True

``` python
for i in dic.keys():
    print(i)
print("*"*50)
for v in dic.values():
    print(v)
```

name
age
address
phone
**************************************************
Peter
34
Texas
1201234

### SET

중복을 허용하지 않는다.
순서가 없다.
    
순서가 없기 때문에 인덱싱을 통해서 자료를 조회할 수 없다.
인덱싱을 사용하려면 리스트, 튜플 자료형으로 변환한 후 사용할 수 있다.
list(), tuple() 변환함수 사용
    
중복을 허용하지 않는 SET 특징을 자료형의 중복을 제거하기 위한 필터 역할로 종종 사용된다.
[1,1,2.22,3,4,5,5,5,6,6]
set([1,1,2.22,3,4,5,5,5,6,6])

``` python
my_set = {'a','b','c','a','b'}
my_set
```
{'a', 'b', 'c'}

``` python
# 중복을 벗겨내는 필터링 역할
a = [1,2,2,3,4,5,5,6,78,2,1]
print(a)

set_a = set(a)
print(set_a)

list_a=list(set_a)
print(list_a)
```
​
list_a=list(set_a)
print(list_a)
[1, 2, 2, 3, 4, 5, 5, 6, 78, 2, 1]
{1, 2, 3, 4, 5, 6, 78}
[1, 2, 3, 4, 5, 6, 78]

``` python
# 문제 1. 위에서 출력된 결과 list_a를 내림차순으로 정렬해보자
# sort 리턴타입이 None이다.

list_sort = list_a.sort(reverse=True)
print(list_sort)

print(sorted(list_a, reverse=True))
```

None
[78, 6, 5, 4, 3, 2, 1]

#### SET 관련 함수
    add() | update() | remove()

``` python
#1. 하나의 값 추가하기...add()
s1 = set([1,2,3])
s1.add(5)
s1

#2. 값 여러개 추가하기...update
s2 = {4,5,6,7}
s1.update(s2)
s1
```

{1, 2, 3, 5}
{1, 2, 3, 4, 5, 6, 7}

``` python
#3. 값 제거하기...remove()

s1.remove(1)
s1
```
{2, 3, 4, 5, 6, 7}