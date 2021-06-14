## Object Detection

### Object Detection 분류

1. Region proposal based
- R-CNN

<br>

2. Regression based
- YOLO

### Object Detection 모델
- 입력 : 사진
- 출력 : 박스 바운더리 정보 & 라벨 정보

![ob1](./img/ob1.jpg)

box1 : (x1,y1,h1,w1,cls1-dog)
box2 : (x2,y2,h2,w2,cls2-car)
box3 : (x3,y3,h3,w3,cls3-bicycle)

<br>

x,y -> 박스의 중심점
w,h -> 박스 height, width
cls -> 출력 카테고리 중 가장 큰 값으로 예측한 값

### R-CNN(Region-CNN)
- 2013년 Segmentation 이전에 나온 모델이다. 이때까지의 CNN은 Classification 밖에 못하는 네트워크였다.

- 1단계 : 이미지 삽입

- 2단계 : Region Proposal을 추출
    - Selective Search Algorithm
        - 색깔별로 덩어리를 묶어준다.
        1. 인공지능 알고리즘이 아니다.
        -> 학습이 되지 않음
        2. 비슷한 색,패턴,질감끼리 묶어주는 알고리즘
        -> 단순해보이지만 내부적으로 복잡

![ob2](./img/ob2.jpg)

하지만 Selective Search Algorithm 만으로는 안 묶이는 것이 굉장히 많아서 box를 굉장히 많이 쳐주게 된다.

따라서 Region Proposal을 하면 거의 2000개에 가까운 box가 만들어졌다.

- 3단계 : 일정한 사이즈로 압축시킨다.
    - 2000개 정도의 Region Proposal로 제안된 모든 이미지들을 동일한 사이즈로 압축시킨다.
    - 이때, 압축된 모든 이미지들이 CNN의 forward 과정에 들어간다.
        - 따라서 Computation Cost가 엄청나다는 최대의 단점이 있다. 이 모델에서 이미지 한 장을 처리하기 위해 1분 정도가 소요된다고 한다.

- 4단계 : 압축된 이미지 2000개가 네트워크를 거쳐서 2000개 하나하나에 대한 분류 작업이 진행한다.(이때, 머신러닝의 모델인 SVM이 사용된다.)
    - 해당 박스들을 작업하면서 객체가 있는지 없는지를 확인한다. 그 후에 객체가 있으면 어떠한 객체인지 분류해준다.

![RCNN](./img/RCNN.png)

4단계에서 Threshold 값이 0.9 이상인 추출 결과만 SVM으로 classification을 진행하여 10개의 최종 출력값과 연결하였다.

SVM을 사용한 이유는 크게 2가지이다.

1. AlexNet(2012)이 나온지 얼마 안된 시점이었다.
2. SVM은 다른 모델들과 비교해서 성능이 꽤 잘 나오는 모델이었기 떄문이다.

#### R-CNN의 문제점
1. 시간이 너무 오래 걸려서 Computation Cost가 많이 들었다.
2. Selective Search algorithm과 SVM은 학습이 되지 않는 알고리즘들이다.

향후 이러한 속도문제를 해결하기 위해서 Fast R-CNN, Faster R-CNN 등이 탄생하기도 했다.

### YOLO(You Only Look Once)
- 딱 한번만 forward 진행한다.
- 기본적으로 LowAndModel, 속도는 빠르고 성능은 조금 떨어지는 모델이다.

![ob3](./img/ob3.jpg)

앞서 봤던 R-CNN과 비교하여 처리 속도가 월등히 빠르다는 것을 볼 수 있다.

YOLO는 실행되는 Computer power에 따라서 차이가 있다. 따라서 논문에 성능 평가 시에는 반드시 무슨 GPU를 사용했는지 기재해야 한다.

![YOLO](./img/YOLO.png)

원래는 GoogleNet을 사용했는데 VGG를 사용해도 상관이 없다. 우선 원본 입력 이미지를 인코딩(특징 추출)하여 레이어를 끝까지 쓰지 않고 중간에 잘랐다.

![ob4](./img/ob4.jpg)

그후, 앞에서 Encoding한 이미지의 특징을 그대로 가지고 있는 것을 저장한 레이어부터 시작이 된다. 
실제로 그림과 같이 학습이 진행하게 되고 FCR로 가기 전에 50,176을 4096으로 줄이ㅔㄱ 된다. 그 후에 reshape하여 7x7x30, 원본 이미지와 동일한 형태로 바꾼다. 

