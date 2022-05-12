### 해당 쿼리문은 solvesql 사이트에서 풀었던 문제의 쿼리를 작성한 것입니다.
### 저작권 문제로 작성한 쿼리문만 올립니다.
<br>

- 첫 주문과 마지막 주문
``` sql
select min(date(order_purchase_timestamp)) as first_order_date,
       max(date(order_purchase_timestamp)) as last_order_date
from olist_orders_dataset
limit 10
```

<br>


- 배송 예정일 예측 성공과 실패
``` sql
select date(order_purchase_timestamp) purchase_date,
       count(case when order_delivered_customer_date <= order_estimated_delivery_date then order_id end) as success,
       count(case when order_delivered_customer_date > order_estimated_delivery_date then order_id end) as fail
from olist_orders_dataset
where order_purchase_timestamp like '2017-01%'
      and order_delivered_customer_date is not null
      and order_estimated_delivery_date is not null
group by purchase_date
order by purchase_date
```

<br>

- 서울북부지방법원 따릉이 정류소
```sql
select *
from station
where lat > (select lat from station where name = '서울북부지방법원')
```

<br>

- 작품이 없는 작가 찾기
```sql
SELECT at.artist_id
     , at.name
FROM artists at
LEFT JOIN artworks_artists aa ON at.artist_id = aa.artist_id
WHERE death_year is not null AND artwork_id is null
```

<br>

- 멘토리 짝궁 리스트
```sql
select mentee.employee_id as mentee_id,
       mentee.name as mentee_name,
       mentor.employee_id as mentor_id,
       mentor.name as mentor_name
from employees mentee, employees mentor
where mentee.join_date between '2021-09-01' and '2021-12-31'
  and mentor.join_date <= '2019.12.31'
  and mentee.department != mentor.department
order by mentee_id, mentor_id
```