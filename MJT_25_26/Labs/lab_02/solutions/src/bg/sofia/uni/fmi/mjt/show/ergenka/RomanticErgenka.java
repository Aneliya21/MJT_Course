package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class RomanticErgenka implements Ergenka {

    private static final int ROMANCE_MULT_CONST = 7;
    private static final int HUMOR_DEL_CONST = 3;

    private static final int DATE_MIN_DURATION = 30;
    private static final int DATE_MAX_DURATION = 120;

    private static final int PLUS_BONUSES_FOR_GOOD_DATE = 5;
    private static final int MINUS_BONUSES_FOR_BAD_DATE = 2;
    private static final int MINUS_BONUSES_FOR_VERY_BAD_DATE = 3;

    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    private int rating;
    private final String favoriteDateLocation;

    public RomanticErgenka(String name, short age,
                           int romanceLevel, int humorLevel,
                           int rating, String favoriteDateLocation) {
        this.name = name;
        this.age = age;
        this.romanceLevel = romanceLevel;
        this.humorLevel = humorLevel;
        this.rating = rating;
        this.favoriteDateLocation = favoriteDateLocation;
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

        int romanceLevelCoef = this.romanceLevel * ROMANCE_MULT_CONST;
        int humorLevelCoef = this.humorLevel / HUMOR_DEL_CONST;

        int tension = (dateEvent == null) ? 1 : Math.max(1, dateEvent.getTensionLevel());
        int romanceContribution = Math.floorDiv(romanceLevelCoef, tension);

        return romanceContribution + humorLevelCoef + bonuses;
    }

    private int calculateBonuses(DateEvent dateEvent) {
        int bonuses = 0;
        if (dateEvent == null) {
            return bonuses;
        }

        if (this.favoriteDateLocation != null && dateEvent.getLocation() != null &&
            this.favoriteDateLocation.equalsIgnoreCase(dateEvent.getLocation())) {
            bonuses += PLUS_BONUSES_FOR_GOOD_DATE;
        }

        if (dateEvent.getDuration() < DATE_MIN_DURATION) {
            bonuses -= MINUS_BONUSES_FOR_VERY_BAD_DATE;
        } else if (dateEvent.getDuration() > DATE_MAX_DURATION) {
            bonuses -= MINUS_BONUSES_FOR_BAD_DATE;
        }

        return bonuses;
    }
}
