package MeetPoint.meetpoint.Map.algorithm;

import java.util.*;

/********************
 * 날짜 : 2024.03.29
 * 이름 : 김준식
 * 내용 : 중간지점
 * ******************/
public class MidPoint {

     /***********************************
      * 날짜 : 2024.03.29
      * 이름 : 김준식
      * 내용 : 무게중심(그라함 스캔 알고리즘)
      * *********************************/
     public HashMap<String, Object> centerOfGravity(List<Double> latitude, List<Double> longitude) { // 위도(y), 경도(x)
         return grahamScan(latitude, longitude);
     }

     //그라함 스캔 알고리즘
     static class Point {
         double lat, lon; // 위도 경도
         public Point(double lat, double lon) {
             this.lat = lat; // 위도 (y)
             this.lon = lon; // 경도 (x)
         }
     }
     Point startPoint = new Point(0,0); // 시작 좌표
     HashMap<String, Object> grahamScan(List<Double> latitude, List<Double> longitude) {
         List<Point> points = new ArrayList<>(); // 좌표들을 저장할 변수

         for(int i=0;i< latitude.size();i++){
             if(latitude.get(i) > startPoint.lat ) {
                 startPoint.lat = latitude.get(i);
                 startPoint.lon = longitude.get(i);
             } else if (latitude.get(i) == startPoint.lat) {
                 if (longitude.get(i) > startPoint.lon) {
                     startPoint.lon = longitude.get(i);
                 }
             }
         }

         // 입력받은 경도와 위도를 리스트에 저장 - 예) [(위도,경도), (위도,경도), . . .]
         for(int i=0; i< latitude.size(); i++){
             points.add(new Point(latitude.get(i), longitude.get(i))); // 각 위치 좌표 저장( 경도(x),위도(y) )
         }

         // 각 위치 중에서 y값이 가장 작은 값을 찾고 그 위치를 기준점으로 잡는다.
         // 위도 값이 가장 작은 값을 찾는다. 만약 위도값이 같을 경우 경도 값이 작은 값을 기준점으로 한다.
         for (MidPoint.Point point : points) {
             if (point.lat < startPoint.lat) {
                 startPoint.lat = point.lat;
                 startPoint.lon = point.lon;
             } else if (point.lat == startPoint.lat) {
                 if (point.lon < startPoint.lon) {
                     startPoint.lon = point.lon;
                 }
             }
         }

         // 모든 좌표들을 반시계 방향으로 정렬하기
         points.sort(new Comparator<Point>() {
             @Override
             public int compare(Point secondPo, Point thirdPo) {
                 int ccwResult = ccw(startPoint, secondPo, thirdPo);
                 if (ccwResult > 0) // 반시계 방향
                     return -1; // 오름차순
                 else if(ccwResult < 0)
                     return 1; // 내림차순
                 else { // ccwResult == 0
                     double distRe = dist(startPoint, secondPo);
                     double distRe2 = dist(startPoint, thirdPo);
                     if(distRe > distRe2)
                         return 1;
                 }
                 return -1;
             }
         });

         // 그라함 스캔 알고리즘
         Stack<Point> stack = new Stack<Point>();
         stack.add(startPoint);
         for(int i=1;i<points.size(); i++){
             // 시계방향이면 제거
             while(stack.size() >1 && ccw(stack.get(stack.size()-2), stack.get(stack.size()-1), points.get(i)) <= 0) {
                 stack.pop();
             }
             stack.add(points.get(i));
         }

         return centerCalculation(stack);
     }

    // CCW( Count-Clock-Wise )는 3점을 이은 선분의 방향성에 대해서 찾는 방법
    // 세 점 A(x1, y1), B(x2, y2), C(x3, y3)에 대해 A, B, C의 기울기를 이용하여 회전 방향을 판단하는 방식
    // 촤측으로 향하는 경우 -> (y2 −y1)(x3−x2)-(x2−x1)(y3−y2) > 0 (즉, 반시계 방향)
    // 우측으로 향하는 경우 -> (y2 −y1)(x3−x2)-(x2−x1)(y3−y2) < 0 (즉, 시계 방향)
    static int ccw(Point p1, Point p2, Point p3){
         double result = (p3.lon-p2.lon)*(p2.lat-p1.lat) - (p2.lon-p1.lon)*(p3.lat-p2.lat);
         if(result > 0) { // 반시계 방향
             return 1;
         }
         else if (result < 0) { // 시계 방향
             return -1;
         }
         else { // 기울기가 일직선일 경우
             return 0;
         }
    }

    //두 점 사이의 거리계산
    static double dist(Point p1, Point p2){
         return (p2.lon-p1.lon)*(p2.lon-p1.lon) + (p2.lat-p1.lat)*(p2.lat-p1.lat);
    }

    //중간 지점 계산
    HashMap<String, Object> centerCalculation(Stack<Point> stack){
         HashMap<String, Object> result = new HashMap<String, Object>();
         double latitude = 0, longitude = 0;
         for (Point point : stack) {

             latitude += point.lat;
             longitude += point.lon;
         }
         result.put("longitude",String.format("%.13f", (longitude/stack.size())));
         result.put("latitude",String.format("%.13f", (latitude/stack.size())));
         return result;
    }

}
