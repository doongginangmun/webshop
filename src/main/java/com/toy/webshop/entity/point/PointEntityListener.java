package com.toy.webshop.entity.point;

import com.toy.webshop.config.BeanUtil;
import com.toy.webshop.repository.PointHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PointEntityListener {
    @PrePersist
    @PreUpdate
    public void prePersistAndPreUpdate(Object o) {
        PointHistoryRepository pointHistoryRepository = BeanUtil.getBean(PointHistoryRepository.class);
        Point point = (Point) o;
        int pointAmount = point.getPrice();

        if(point.getStatus()==PointStatus.사용) {
            List<PointHistory> pointHistories = pointHistoryRepository.findByUserId(point.getUser().getId());
            int[] plusPointAmounts = getPlusPointAmounts(pointHistories);
            int[] minusPointAmounts = getMinusPointAmounts(pointHistories);
            boolean isPointAmountZero = false;

             /*
                앞에서부터 히스토리에 있는 적립된(plus) 포인트와 사용된(minus) 포인트를 더해,
                남은 포인트를 계산
             */
            List<Integer> currentRemainPointAmounts =
                    getCurrentRemainPointAmounts(plusPointAmounts, minusPointAmounts);

            //앞에서 부터 포인트를 차감하며 history 에 저장
            for (Integer frontPointAmount : currentRemainPointAmounts) {
                if(isPointAmountZero) break;
                PointHistory pointHistory;
                if(frontPointAmount + pointAmount <= 0) { //
                    pointAmount += frontPointAmount;
                    pointHistory = PointHistory.createPointHistory(point, frontPointAmount * -1);
                }else {
                    pointHistory = PointHistory.createPointHistory(point, pointAmount);
                    pointAmount = 0;
                }
                pointHistoryRepository.save(pointHistory);
                if(pointAmount == 0) isPointAmountZero = true;
            }

        }else { //적립일경우 히스토리에 값 그대로 저장
            PointHistory pointHistory = PointHistory.createPointHistory(point, pointAmount);
            pointHistoryRepository.save(pointHistory);
        }
    }

    private int[] getMinusPointAmounts(List<PointHistory> pointHistories) {
        return pointHistories.stream()
                .filter(pointHistory -> pointHistory.getTradePoint() < 0)
                .mapToInt(PointHistory::getTradePoint)
                .toArray();
    }

    private int[] getPlusPointAmounts(List<PointHistory> pointHistories) {
        return pointHistories.stream()
                .filter(pointHistory -> pointHistory.getTradePoint() > 0)
                .mapToInt(PointHistory::getTradePoint)
                .toArray();
    }

    private List<Integer> getCurrentRemainPointAmounts(int[] plusPointAmounts, int[] minusPointAmounts) {
        List<Integer> presentRemainPoints = new ArrayList<>();
        boolean hasPlusAndMinusPointAmount = !ObjectUtils.isEmpty(plusPointAmounts) && !ObjectUtils.isEmpty(minusPointAmounts);
        boolean hasPlusPointAmount = ObjectUtils.isEmpty(minusPointAmounts);

        if(hasPlusAndMinusPointAmount) {
            calcCurrentRemainPointAmount(plusPointAmounts, minusPointAmounts);

            for (int plusPoint : plusPointAmounts) {
                if(plusPoint != 0)
                    presentRemainPoints.add(plusPoint);
            }
        } else if(hasPlusPointAmount) {
            presentRemainPoints = Arrays.stream(plusPointAmounts)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return presentRemainPoints;
    }

    private void calcCurrentRemainPointAmount(int[] plusPointAmounts, int[] minusPointAmounts) {
        int plusPointIndex = 0;
        int minusPointIndex = 0;

        while (plusPointIndex < plusPointAmounts.length && minusPointIndex < minusPointAmounts.length) {
            plusPointAmounts[plusPointIndex] += minusPointAmounts[minusPointIndex];
            if (plusPointAmounts[plusPointIndex] == 0) plusPointIndex++;
            minusPointIndex++;
        }
    }
}
