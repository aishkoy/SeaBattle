import java.util.Scanner;

public class Bonus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите предыдущее показание электросчетчика: ");
        int previousReading = sc.nextInt();
        System.out.print("Введите текущее показание электросчетчика: ");
        int currentReading = sc.nextInt();
        System.out.print("Введите количество проживающих: ");
        int people = sc.nextInt();

        int total = Math.abs(currentReading - previousReading);

        int firstLevelLimit = 115 * people;
        int secondLevelLimit = 190 * people;

        int firstLevel = Math.min(firstLevelLimit, total);
        int secondLevel = Math.min(secondLevelLimit - firstLevelLimit, total - firstLevelLimit);
        int thirdLevel = total - secondLevelLimit;

        System.out.println();
        System.out.println("Вы потребили: ");
        System.out.println("Первый уровень: " + firstLevel + " кВт");
        System.out.println("Второй уровень: " + secondLevel + " кВт");
        System.out.println("Третий уровень: " + thirdLevel + " кВт");

        float firstLevelTariff = 1.2f;
        float secondLevelTariff = 2.16f;
        float thirdLevelTariff = 3.28f;

        System.out.println();
        System.out.println("Тариф: " + firstLevelTariff + " сом, " + secondLevelTariff + " сом, " + thirdLevelTariff + " сом");

        float firstLevelResult = firstLevel * firstLevelTariff;
        float secondLevelResult = secondLevel * secondLevelTariff;
        float thirdLevelResult = thirdLevel * thirdLevelTariff;

        double first = Math.round(firstLevelResult * 10) / 10.0;
        double second = Math.round(secondLevelResult * 10) / 10.0;
        double third = Math.round(thirdLevelResult * 10) / 10.0;

        int totalResult = (int) Math.ceil(first + second+ third);


        System.out.println("К оплате:");
        System.out.println("Первый уровень: " + first + " сом");
        System.out.println("Второй уровень: " + second + " сом");
        System.out.println("Третий уровень: " + third + " сом");
        System.out.println("Общая сумма: " + totalResult+ " сом");
        sc.close();
    }
}

