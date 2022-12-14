package bot.service;

import bot.database.entites.*;

import java.util.*;

public class TariffReady {

    private final String name;
    private final int price;
    private final String shortDesc;
    private final long tariff_id;


    public String getName() {
        return name;
    }

    private final List<Equip> equip;
    private final List<Extra> extra;
    private final List<Service> services;
    private final List<Tags> tags;

    public int getPrice() {
        return price;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public List<Equip> getEquip() {
        return equip;
    }

    public List<Extra> getExtra() {
        return extra;
    }

    public List<Service> getServices() {
        return services;
    }

    public List<Tags> getTags() {
        return tags;
    }

    private boolean hasDiscount;

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public long getTariff_id() {
        return tariff_id;
    }

    private int discountPrice;

    private Date endOfAction;

    public Date getEndOfAction(){return this.endOfAction;}

    public void makeDiscount(int price, Long end){
        this.discountPrice = price;
        this.endOfAction = new Date(end);
        hasDiscount = true;
    }

    public void unmakeDiscount(){
        discountPrice = 0;
        hasDiscount = false;
    }

    private Map<Long, Timer> timersMap = new HashMap<>();


    class DiscountDeleteTask extends TimerTask{

        private long discountId;
        public DiscountDeleteTask(long id){
            this.discountId = id;
        }

        @Override
        public void run() {
            DataManager.getInstance().deleteDiscount(this.discountId);
            timersMap.get(this.discountId).cancel();
            timersMap.remove(this.discountId);
        }
    }


    private TariffReady(TariffBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.shortDesc = builder.shortDesc;
        this.equip = builder.equip;
        this.extra = builder.extra;
        this.services = builder.services;
        this.tags = builder.tags;
        this.hasDiscount = builder.hasDiscount;
        this.discountPrice = builder.discountPrice;
        this.tariff_id = builder.tariff_id;
        this.endOfAction = builder.endOfAction;

        if(builder.hasDiscount){
            for(Discount d:DataManager.getInstance().getDiscountData()){
                if(d.getTariff_id() == builder.tariff_id){
                    Timer t = new Timer();
                    t.schedule(new DiscountDeleteTask(d.getID()), builder.endOfAction);
                    timersMap.put(d.getID(), t);
                }
            }
        }
    }

    public String toString(){

        StringBuilder equipF = new StringBuilder();
        equipF.append(equip.size()==0?"":"&#9881;????????????????????????: \n");
        for(Equip e:equip){
            equipF.append(e.getValue() + "\n");
        }
        StringBuilder extraF = new StringBuilder();
        extraF.append(extra.size()==0?"":"&#10133;??????????????????????????: \n");
        for(Extra e :extra){
            extraF.append(e.getValue() + "\n");
        }
        StringBuilder serviceF = new StringBuilder();
        serviceF.append("??????????????: \n");
        for(Service s:services){
            serviceF.append(s.getEmoji() + s.getName() + " " + s.getValue() + "\n");
        }
        StringBuilder tagsF = new StringBuilder();
        tagsF.append("????????: ");
        for(Tags t:tags){
            tagsF.append(t.getName() + " ");
        }

        return (hasDiscount?"&#10071;?????????? ???? ??????????\n":"") +
                name + "\n" +
                "????????: " + (hasDiscount?discountPrice:price) + " ????????????/??????????\n\n" +
                shortDesc + "\n\n" +
                serviceF + "\n" +
                equipF +
                extraF +
                tagsF + "\n";
    }

    public static class TariffBuilder{
        private final String name;
        private final int price;
        private final String shortDesc;
        private final long tariff_id;

        private List<Equip> equip;
        private List<Extra> extra;
        private List<Service> services;
        private List<Tags> tags;
        private boolean hasDiscount;
        private int discountPrice;
        private Date endOfAction;

        public TariffBuilder(String name, int price, String shortDesc, long tariff_id) {
            this.name = name;
            this.price = price;
            this.shortDesc = shortDesc;
            this.tariff_id = tariff_id;
        }

        public TariffBuilder equip(List<Equip> equip){
            this.equip = equip;
            return this;
        }

        public TariffBuilder extra(List<Extra> extra){
            this.extra = extra;
            return this;
        }

        public TariffBuilder services(List<Service> services){
            this.services = services;
            return this;
        }

        public TariffBuilder tags(List<Tags> tags){
            this.tags = tags;
            return this;
        }

        public TariffBuilder discount(int price, Long end){
            this.hasDiscount = true;
            this.discountPrice = price;
            this.endOfAction = new Date(end);
            return this;
        }

        public TariffReady build(){
            return new TariffReady(this);
        }
    }
}
