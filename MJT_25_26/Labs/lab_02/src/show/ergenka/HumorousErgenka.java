package Labs.lab_02.src.ergenka;

import Labs.lab_02.src.date.DateEvent;

public class HumorousErgenka implements Ergenka {

    private static final int HUMOR_MULT_CONST =  5;
    private static final int ROMANCE_DEL_CONST = 3;

    private static final int DATE_MIN_DURATION = 30;
    private static final int DATE_MAX_DURATION = 90;

    private static final int PLUS_BONUSES_FOR_GOOD_DATE = 4;
    private static final int MINUS_BONUSES_FOR_BAD_DATE = 2;
    private static final int MINUS_BONUSES_FOR_VERY_BAD_DATE = 3;

    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    private int rating;

    public HumorousErgenka(String name, short age,
                           int romanceLevel, int humorLevel,
                           int rating) {
        this.name = name;
        this.age = age;
        this.romanceLevel = romanceLevel;
        this.humorLevel = humorLevel;
        this.rating = rating;
    }

    @Override
    public String getName() {
        if (name == null || name.isEmpty()) {
            return "";
        }

        return name;
    }

    @Override
    public short getAge() {
        return age;
    }

    @Override
    public int getRomanceLevel() {
        return romanceLevel;
    }

    @Override
    public int getHumorLevel() {
        return humorLevel;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void reactToDate(DateEvent dateEvent) {
        this.rating = calculateRating(dateEvent);
    }

    private int calculateRating(DateEvent dateEvent) {
        int bonuses = calculateBonuses(dateEvent);

        int humorLevelCoef = this.humorLevel * HUMOR_MULT_CONST;
        int romanceLevelCoef = this.romanceLevel / ROMANCE_DEL_CONST;

        int tension = (dateEvent == null) ? 1 : Math.max(1, dateEvent.getTensionLevel());
        int humorContribution = Math.floorDiv(humorLevelCoef, tension);

        return humorContribution + romanceLevelCoef + bonuses;
    }

    private int calculateBonuses(DateEvent dateEvent) {
        int bonuses = 0;
        if (dateEvent == null) {
            return bonuses;
        }

        if (dateEvent.getDuration() >= DATE_MIN_DURATION && dateEvent.getDuration() <= DATE_MAX_DURATION) {
            bonuses += PLUS_BONUSES_FOR_GOOD_DATE;
        } else if (dateEvent.getDuration() < DATE_MIN_DURATION) {
            bonuses -= MINUS_BONUSES_FOR_BAD_DATE;
        } else {
            bonuses -= MINUS_BONUSES_FOR_VERY_BAD_DATE;
        }

        return bonuses;
    }
}
