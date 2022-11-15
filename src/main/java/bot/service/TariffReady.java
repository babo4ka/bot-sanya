package bot.service;

import bot.database.entites.Equip;
import bot.database.entites.Extra;
import bot.database.entites.Service;
import bot.database.entites.Tags;

import java.util.List;

public class TariffReady {

    private final String name;
    private final int price;
    private final String shortDesc;


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

    private TariffReady(TariffBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.shortDesc = builder.shortDesc;
        this.equip = builder.equip;
        this.extra = builder.extra;
        this.services = builder.services;
        this.tags = builder.tags;
    }

    public String toString(){
        StringBuilder equipF = new StringBuilder();
        equipF.append(equip.size()==0?"":"&#9881;Оборудование: \n");
        for(Equip e:equip){
            equipF.append(e.getValue() + "\n");
        }
        StringBuilder extraF = new StringBuilder();
        extraF.append(extra.size()==0?"":"&#10133;Дополнительно: \n");
        for(Extra e :extra){
            extraF.append(e.getValue() + "\n");
        }
        StringBuilder serviceF = new StringBuilder();
        serviceF.append("Сервисы: \n");
        for(Service s:services){
            serviceF.append(s.getEmoji() + s.getName() + " " + s.getValue() + "\n");
        }
        StringBuilder tagsF = new StringBuilder();
        tagsF.append("Теги: ");
        for(Tags t:tags){
            tagsF.append(t.getName() + " ");
        }

        return name + "\n" +
                "Цена: " + price + "\n\n" +
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

        private List<Equip> equip;
        private List<Extra> extra;
        private List<Service> services;
        private List<Tags> tags;

        public TariffBuilder(String name, int price, String shortDesc) {
            this.name = name;
            this.price = price;
            this.shortDesc = shortDesc;
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


        public TariffReady build(){
            return new TariffReady(this);
        }
    }
}
