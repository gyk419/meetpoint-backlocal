package MeetPoint.meetpoint.Map.algorithm;

import java.util.*;

/**
 * 작성일 : 2024.03.29
 * 작성자 : 김준식
 * 내용 : 중간지점
 **/
public class MidPoint {
    /**
     +     * 작성일 : 2024.04.10
     +     * 작성자 : 김준식
     +     * 내용 : 거리순
     +     **/
    public HashMap<String, Object> distance(List<Double> latitude, List<Double> longitude) {
        List<Point> points = new ArrayList<>();
        HashMap<String, Object> resultValue = new HashMap<>();
        for(int i = 0; i < latitude.size(); i++){
            points.add(new Point(latitude.get(i), longitude.get(i)));
        }
        System.out.println("midpoint 첫번째");

        // 평균 이동 알고리즘 적용
       List<Point> result = meanShift(points, 1.0, 0.05);

        // 수렴한 중간 지점들 출력
        System.out.println("수렴한 중간 지점들:");
        for (Point centroid : result) {
           System.out.println("(" + centroid.lat + ", " + centroid.lon + ")");
        }
        Point centroid  = result.get(result.size()-1);
        resultValue.put("latitude", centroid.lat);
        resultValue.put("longitude", centroid.lon);
        return resultValue;
    }
    public static double euclideanDistance(Point a, Point b) {
        // 두 점 사이의 유클리드 거리를 계산하는 메서드
                double dx = a.lon - b.lon;
        double dy = a.lat - b.lat;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Point findNewCentroid(Point point, List<Point> points, double kernelBandwidth) {
        // 주어진 점에 대해 새로운 중심을 찾는 메서드
        double[] weights = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            double distance = euclideanDistance(point, points.get(i));
            weights[i] = Math.exp(-0.5 * Math.pow(distance / kernelBandwidth, 2));
        }

        double sumLat = 0;
        double sumLon = 0;
        double sumWeights = 0;

        for (int i = 0; i < points.size(); i++) {
            sumLat += points.get(i).lat * weights[i];
            sumLon += points.get(i).lon * weights[i];
            sumWeights += weights[i];
        }

        double newLat = sumLat / sumWeights;
        double newLon = sumLon / sumWeights;

        return new Point(newLat, newLon);
    }

    public static List<Point> meanShift(List<Point> points, double kernelBandwidth, double convergenceThreshold) {
        // 평균 이동 알고리즘을 수행하는 메서드
        List<Point> centroids = new ArrayList<>(points);

        for (int i = 0; i < centroids.size(); i++) {
        Point point = centroids.get(i);
        while (true) {
                Point newCentroid = findNewCentroid(point, points, kernelBandwidth);
                double shift = euclideanDistance(newCentroid, point);
                if (shift < convergenceThreshold) {
                        centroids.set(i, newCentroid);
                        break;
                    }
                point = newCentroid;
            }
        }

        return centroids;
    }


     /**
      * 작성일 : 2024.03.29
      * 작성자 : 김준식
      * 내용 : 무게중심(그라함 스캔 알고리즘)
      **/
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
         result.put("longitude",String.format("%.15f", (longitude/stack.size())));
         result.put("latitude",String.format("%.15f", (latitude/stack.size())));
         return result;
    }

    /**
     * 작성일 : 2024.04.07
     * 작성자 : 김준식
     * 내용 : 교통점수순
     **/
    public HashMap<String, Object> vehiclesScore(List<Double> latitude, List<Double> longitude, double lowScoreLat, double lowScoreLon, int optionValue) {
        HashMap<String, Object> middlePoint = grahamScan(latitude,longitude); // 중간 좌표
        double mpLat = Double.parseDouble(middlePoint.get("latitude").toString()); // 중간좌표 위도
        double mpLon = Double.parseDouble(middlePoint.get("longitude").toString()); // 중간좌표 경도
        System.out.println("maLat : " + mpLat);
        System.out.println("mpLon : " + mpLon);
        HashMap<String, Object> result = new HashMap<>(); // 결과 반환

        // 똑같은 점수를 받은 좌표들이 없을 경우 1(즉, 점수가 다 다를 경우)
        if (optionValue == 1) {
            double newLat = Math.abs(mpLat - lowScoreLat) / 3; // (중간좌표의 위도값 - 교통점수가 낮은 좌표의 위도 값) / 3을 하여 3분의 1지점 지정
            double newLon = Math.abs(mpLon - lowScoreLon) / 3; // (중간좌표의 경도값 - 교통점수가 낮은 좌표의 경도 값) / 3을 하여 3분의 1지점 지정
            System.out.println("newLat: " + newLat);
            System.out.println("newLon: " + newLon);

            //중간좌표와 점수가 낮은 좌표의 위도가 같을 경우 ( 중간좌표 위도 == 낮은 점수 좌표 위도)
            if(mpLat == lowScoreLat){
                if (mpLon > lowScoreLon){ // 중간 좌표 경도 값이 점수가 낮은 경도 값보다 클 경우 ( 중간 좌표 경도 > 낮은 점수 좌표 경도)
                    mpLon -= newLon;
                } else { // 중간 좌표 경도 값이 점수가 낮은 경도 값보다 작을 경우 ( 중간 좌표 경도 < 낮은 점수 좌표 경도)
                    mpLon += newLon;
                }
            }

            // 중간좌표와 점수가 낮은 좌표의 경도가 같을 경우 ( 중간좌표 경도 == 점수 낮은 좌표 경도)
            if(mpLon == lowScoreLon){
                if (mpLat > lowScoreLat) { // 중간 좌표 위도 값이 점수가 낮은 위도 값보다 클 경우 ( 중간 좌표 위도 > 낮은 점수 좌표 위도 )
                    mpLat -= newLat;
                } else { // 중간 좌표 위도 값이 점수가 낮은 위도 값보다 작을 경우 ( 중간 좌표 위도 < 낮은 점수 좌표 위도 )
                    mpLat += newLat;
                }
            }

            // 중간 좌표 위도,경도 값이 낮은 점수 좌표 위도,경도 갑보다 작을 경우 ( 중간좌표 위도, 경도 < 낮은 점수 좌표 위도,경도 )
            if(mpLat < lowScoreLat && mpLon < lowScoreLon) {
                mpLat += newLat;
                mpLon += newLon;
            }

            // 중간 좌표 경도값이 낮은 점수 좌표 경도 값보다 크고, 중간좌표 위도 값이 낮은 점수 좌표 위도 값보다 작을 경우 ( 중간좌표 경도 > 낮은 점수 좌표 경도 AND 중간좌표 위도 <  낮은 점수 좌표 위도 )
            if(mpLon > lowScoreLon && mpLat < lowScoreLat) {
                mpLat += newLat;
                mpLon -= newLon;
            }

            // 중간 좌표 위도, 경도 값이 낮은 점수 좌표 위도, 경도 값보다 클 경우 ( 중간좌표 위도, 경도 > 낮은 점수 좌표 위도, 경도 )
            if(mpLat > lowScoreLat && mpLon > lowScoreLon){
                mpLat -= newLat;
                mpLon -= newLon;
            }

            // 중간 좌표 경도값이 점수 낮은 좌표 경도 값보다 작고, 중간좌표 위도값이 점수 낮은 좌표 위도 값보다 클 경우 ( 중간좌표 경도 < 낮은 점수 좌표 경도 AND 중간좌표 위도 > 낮은 점수 좌표 위도 )
            if(mpLon < lowScoreLon && mpLat > lowScoreLat){
                mpLon += newLon;
                mpLat -= newLat;
            }

            result.put("latitude", mpLat);
            result.put("longitude", mpLon);
        } else { // 똑같은 점수를 받은 좌표들이 여러개일 경우
            result.put("latitude", (lowScoreLat + mpLat) / (optionValue + 1) ); // 여러개의 좌표들의 위도를 합한 값과 중간 좌표 위도값을 더한 후 더한 갯수 만큼 나누어 무게중심 구함.
            result.put("longitude", (lowScoreLon + mpLon) / (optionValue + 1) ); // 여러개의 좌표들의 경도를 합한 값과 중간 좌표 경도값을 더한 후 더한 갯수 만큼 나누어 무게중심 구함.
        }

        return result;
    }
}
