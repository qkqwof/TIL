## 변수와 데이터 타입
파이썬에서의 데이터 타입들
        
int - 정수
float - 실수
bool - False, True
str - 문자열
list - 리스트, 순서가 있는 값들의 집합 [], 값은 변경 가능
tuple - 튜플, 순서가 있는 값들의 집합 (), 값은 변경 불가
set - 순서없고 중복 안되는 값들의 집합 {}
dic - 키와 값이 쌍으로 저장되는 값들의 집합 {"key":value}

``` python
a = 3
b = 3.5
c = "Hello"
d = 'Hello'

print(a)
print(b)
print(c)
print(d)

print('*'+'*') # 문자열 + 문자열 : 문자열이 더해진다.
print('*' * 30) # 연산도 가능하다.

# 값들의 타입을 출력: type() 함수를 이용
print(type(a))
print(type(b))
print(type(c))
print(type(d))

e = 3
f = 3.0
print(e == f)

print(type(e) == type(f))

```

3
3.5
Hello
Hello
**
******************************
<class 'int'>
<class 'float'>
<class 'str'>
<class 'str'>

True
False

## 실행시 값을 입력받는 함수 input()

```python
# input()로 입력된 값을 타입이 무조건 str이다.
data = input("값입력")
print("입력된 값: ",data)
print(type(data))

newdata = int(data)
print(type(newdata))
```

값입력12
입력된 값:  12
<class 'str'>
<class 'int'>

### 연산자와 포맷팅

```python
num1 = 5
num2 = 3
print(num1/num2) # float로 반환
print(num1//num2) # 나눈 몫을 구함...나머지는 버린다.
remain = num1 % num2
print(remain)

'''
설명이 긴 주석처리...
파이썬의 모든 변수는 객체로 취급된다.
그렇기 때문에 당연히 메소드가 있다.
'''
dir(num1)

name = "James"
age = 40
print(name,"의 나이는",age,"세 입니다.")
```

1.6666666666666667
1
2

James 의 나이는 40 세 입니다.

```python
'''
아래에 출력되는 것들은 파이썬 파일을 만들어서 사용하는 순간
메모리에 무조건 올라가는 것들이다.
내장 함수들...
내장함수명이 변수명으로 사용되지 않도록 주의
list(), int(), input(), min(), max()...가 대표적인 내장함수들이다.
'''
dir(__builtin__)
```

['ArithmeticError',
 'AssertionError',
 'AttributeError',
 'BaseException',
 'BlockingIOError',
 'BrokenPipeError',
 'BufferError',
 'BytesWarning',
 'ChildProcessError',
 'ConnectionAbortedError',
 'ConnectionError',
 'ConnectionRefusedError',
 'ConnectionResetError',
 'DeprecationWarning',
 'EOFError',
 'Ellipsis',
 'EnvironmentError',
 'Exception',
 'False',
 'FileExistsError',
 'FileNotFoundError',
 'FloatingPointError',
 'FutureWarning',
 'GeneratorExit',
 'IOError',
 'ImportError',
 'ImportWarning',
 'IndentationError',
 'IndexError',
 'InterruptedError',
 'IsADirectoryError',
 'KeyError',
 'KeyboardInterrupt',
 'LookupError',
 'MemoryError',
 'ModuleNotFoundError',
 'NameError',
 'None',
 'NotADirectoryError',
 'NotImplemented',
 'NotImplementedError',
 'OSError',
 'OverflowError',
 'PendingDeprecationWarning',
 'PermissionError',
 'ProcessLookupError',
 'RecursionError',
 'ReferenceError',
 'ResourceWarning',
 'RuntimeError',
 'RuntimeWarning',
 'StopAsyncIteration',
 'StopIteration',
 'SyntaxError',
 'SyntaxWarning',
 'SystemError',
 'SystemExit',
 'TabError',
 'TimeoutError',
 'True',
 'TypeError',
 'UnboundLocalError',
 'UnicodeDecodeError',
 'UnicodeEncodeError',
 'UnicodeError',
 'UnicodeTranslateError',
 'UnicodeWarning',
 'UserWarning',
 'ValueError',
 'Warning',
 'WindowsError',
 'ZeroDivisionError',
 '__IPYTHON__',
 '__build_class__',
 '__debug__',
 '__doc__',
 '__import__',
 '__loader__',
 '__name__',
 '__package__',
 '__spec__',
 'abs',
 'all',
 'any',
 'ascii',
 'bin',
 'bool',
 'breakpoint',
 'bytearray',
 'bytes',
 'callable',
 'chr',
 'classmethod',
 'compile',
 'complex',
 'copyright',
 'credits',
 'delattr',
 'dict',
 'dir',
 'display',
 'divmod',
 'enumerate',
 'eval',
 'exec',
 'filter',
 'float',
 'format',
 'frozenset',
 'get_ipython',
 'getattr',
 'globals',
 'hasattr',
 'hash',
 'help',
 'hex',
 'id',
 'input',
 'int',
 'isinstance',
 'issubclass',
 'iter',
 'len',
 'license',
 'list',
 'locals',
 'map',
 'max',
 'memoryview',
 'min',
 'next',
 'object',
 'oct',
 'open',
 'ord',
 'pow',
 'print',
 'property',
 'range',
 'repr',
 'reversed',
 'round',
 'set',
 'setattr',
 'slice',
 'sorted',
 'staticmethod',
 'str',
 'sum',
 'super',
 'tuple',
 'type',
 'vars',
 'zip']

``` python
'''
숫자(int, float), 문자(str), Tuple 타입은 immutable 객체이다.(만드는 순간 변경될 수 없는 것)
데이터가 변경되는 순간 새로운 객체가 생성된다.
기존의 데이터를 변경하는 것은 불가능하다.
'''

a = 257
b = 257
print(a == b)
print(a is b) #a와 b가 같은 객체니?? False

print(id(a)) # 주소값
print(id(b)) # 주소값
```

True
False
1309414375728
1309414375568

``` python
a = 200
b = 200

#값 비교
print(a == b)
# 객체 비교  === True가 나온다??
print(a is b)

print(id(a))
print(id(b))
```

True
True
140735194808320
140735194808320

-5 ~ 256 사이의 값들은
메모리를 효율적으로 사용하기 위해서
이 범위에 있는 값들은 한번만 객체를 만들고 나서 만들지 않는다.

```python
mystr1 = 'hello'
mystr2 = "hello"

# 여러줄의 문자열을 표시할 때
mystr3 = '''
            this is 
            mystr3 Test
            hello...
        '''

print(mystr1)
print(mystr2)
print(mystr3)
```

```python
name = "손흥민" # %s
age = 29 # %d
grade = 4.789 #%f

#포맷팅 처리해서 출력하기
print('%s의 나이는 %d, 이번경기 평점은 %.2f입니다.' %(name,age,grade))

#포맷팅2
import time
now = time.localtime()

print('현재 시간은 %d년 %d월 %d일 입니다.' %(now.tm_year,now.tm_mon,now.tm_mday))
```

손흥민의 나이는 29, 이번경기 평점은 4.79입니다.
현재 시간은 2021년 5월 10일 입니다.