package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI {

    private StoreItem[] items;
    private boolean isDiscountApplied = false;


    public GameStore(StoreItem[] availableItems) {
        if(availableItems.length > 0) {
            this.items = new StoreItem[availableItems.length];
            System.arraycopy(availableItems, 0, this.items, 0, availableItems.length);
        }
        else {
            this.items = new StoreItem[0];
        }
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        if(itemFilters.length > 0) {
            StoreItem[] result = new StoreItem[items.length];

            int index = 0;

            for (int i = 0; i < items.length; i++) {

                if (i < itemFilters.length && itemFilters[i].matches(this.items[i])) {
                    result[index++] = this.items[i];
                }
            }
            StoreItem[] finalResult = new StoreItem[index];
            System.arraycopy(result, 0, finalResult, 0, index);
            return finalResult;
        }
        else {
            return items;
        }
    }

    static final BigDecimal discountMultiplier = new BigDecimal("0.40");

    @Override
    public void applyDiscount(String promoCode) {
        if(isDiscountApplied){
            return;
        }

        if(promoCode.equals("VAN40")) {
            for(StoreItem item : this.items) {

                BigDecimal currentPrice = item.getPrice();
                BigDecimal discount = currentPrice.multiply(discountMultiplier);
                BigDecimal newPrice = currentPrice.subtract(discount);
                item.setPrice(newPrice);
            }
            isDiscountApplied = true;
        }
        else if(promoCode.equals("100YO")) {
            for(StoreItem item : this.items) {
                item.setPrice(BigDecimal.ZERO);
            }
            isDiscountApplied = true;
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        if (item == null) {
            return false;
        }

        for (StoreItem storeItem : this.items) {
            if (storeItem.equals(item)) {
                storeItem.rate(rating);
                return true;
            }
        }

        return false;
    }
}
