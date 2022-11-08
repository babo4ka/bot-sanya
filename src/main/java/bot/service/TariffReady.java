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

    private final List<Equip> equip;
    private final List<Extra> extra;
    private final List<Service> services;
    private final List<Tags> tags;

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
        equipF.append("Оборудование: ");
        for(Equip e:equip){
            equipF.append(e.getValue() + "\n");
        }
        StringBuilder extraF = new StringBuilder();
        extraF.append(extra.size()==0?"":"Дополнительно: ");
        for(Extra e :extra){
            extraF.append(e.getValue() + "\n");
        }
        StringBuilder serviceF = new StringBuilder();
        serviceF.append("Сервисы: ");
        for(Service s:services){
            serviceF.append(s.getName() + "\n" +
                    s.getValue() + "\n");
        }
        StringBuilder tagsF = new StringBuilder();
        tagsF.append("Теги: ");
        for(Tags t:tags){
            tagsF.append(t.getName() + "\n");
        }

        return "Название тарифа: " + name + "\n" +
                "Цена: " + price + "\n" +
                shortDesc +
                equipF +
                extraF +
                serviceF +
                tagsF;
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
