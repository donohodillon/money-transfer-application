public class whiteboard{
    public static long[] powersOfTwo(int n){
        long[] list = new long[n + 1];
        for (int i = 0; i < list.length; i++){
            list[i] = (long) Math.pow(2,i);
        }

        return list;
    }

    public static void main(String[] args) {
        System.out.println(powersOfTwo(4));
    }
}


// I want to iterate through this string backwards
