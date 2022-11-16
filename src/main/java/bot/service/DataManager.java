package bot.service;

import bot.database.entites.*;
import bot.database.repositories.DiscountRepository;
import bot.database.repositories.TariffRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    TariffRepository tariffRepository;

    public TariffRepository getTariffRepository() {
        return tariffRepository;
    }

    List<Equip> equipData = new ArrayList<>();
    List<Extra> extraData = new ArrayList<>();
    List<Service> serviceData = new ArrayList<>();
    List<Tags> tagsData = new ArrayList<>();

    List<Tariff> tariffsData = new ArrayList<>();

    List<Equip_inter> equipInterData = new ArrayList<>();
    List<Extra_inter> extraInterData = new ArrayList<>();
    List<Service_inter> serviceInterData = new ArrayList<>();
    List<Tags_inter> tagsInterData = new ArrayList<>();


    DiscountRepository discountRepository;

    public DiscountRepository getDiscountRepository() {
        return discountRepository;
    }

    List<Discount> discountData = new ArrayList<>();

    public List<Discount> getDiscountData() {
        return discountData;
    }
    public void setDiscountData(List<Discount> discountData){
        this.discountData = discountData;
        setAllTariffs();
    }

    List<Subs> subsData = new ArrayList<>();

    public List<Subs> getSubsData(){
        return subsData;
    }

    public boolean isSub(long chatId){
        for(Subs s:subsData){
            if(s.getID() == chatId)return true;
        }
        return false;
    }

    public void setSubsData(List<Subs> subsData) {
        this.subsData = subsData;
    }

    public static DataManager instance;

    public static DataManager getInstance(){
        return instance;
    }

    public static DataManager getInstance(List<Equip> equipData,
                                          List<Extra> extraData,
                                          List<Service> serviceData,
                                          List<Tags> tagsData,
                                          List<Tariff> tariffsData,
                                          List<Equip_inter> equipInterData,
                                          List<Extra_inter> extraInterData,
                                          List<Service_inter> serviceInterData,
                                          List<Tags_inter> tagsInterData,
                                          List<Subs> subsData,
                                          List<Discount> discountData,
                                          TariffRepository tariffRepository,
                                          DiscountRepository discountRepository){
        if(instance == null) {
            instance = new DataManager(
                    equipData, extraData, serviceData, tagsData, tariffsData, equipInterData, extraInterData,
                    serviceInterData, tagsInterData, subsData, discountData, tariffRepository, discountRepository
            );
        }
            return instance;
    }

    private DataManager(List<Equip> equipData,
                       List<Extra> extraData,
                       List<Service> serviceData,
                       List<Tags> tagsData,
                       List<Tariff> tariffsData,
                       List<Equip_inter> equipInterData,
                       List<Extra_inter> extraInterData,
                       List<Service_inter> serviceInterData,
                       List<Tags_inter> tagsInterData,
                        List<Subs> subsData,
                        List<Discount> discountData,
                        TariffRepository tariffRepository,
                        DiscountRepository discountRepository) {
        this.equipData = equipData;
        this.extraData = extraData;
        this.serviceData = serviceData;
        this.tagsData = tagsData;
        this.tariffsData = tariffsData;
        this.equipInterData = equipInterData;
        this.extraInterData = extraInterData;
        this.serviceInterData = serviceInterData;
        this.tagsInterData = tagsInterData;
        this.subsData = subsData;
        this.discountData = discountData;
        this.tariffRepository = tariffRepository;
        this.discountRepository = discountRepository;

        setAllTariffs();
    }

    List<TariffReady> alltariffs = new ArrayList<>();
    Map<Long, TariffReady> tariffReadyById = new HashMap<>();

    public Map<Long, TariffReady> getTariffReadyById() {
        return tariffReadyById;
    }

    public List<TariffReady> getAlltariffs() {
        return alltariffs;
    }

    private int hasDiscount(long id){
        int has = -1;
        for(Discount d: discountData){
            if(d.getTariff_id() == id){
                has = d.getPrice();
                break;
            }
        }

        return has;
    }

    private void setAllTariffs(){
        alltariffs = new ArrayList<>();
        for(Tariff tariff : tariffsData){
            List<Equip> equip = new ArrayList<>();
            List<Equip_inter> equip_inter = equipInterData.stream()
                    .filter(e->e.getTariff_id() == tariff.getID()).toList();

            for(Equip_inter ei : equip_inter){
                for(Equip e : equipData){
                    if(e.getID() == ei.getEquip_id()){
                        equip.add(e);
                        break;
                    }
                }
            }

            List<Service> services = new ArrayList<>();
            List<Service_inter> service_inter = serviceInterData.stream().
                    filter(s->s.getTariff_id() == tariff.getID()).toList();

            for(Service_inter si : service_inter){
                for(Service s : serviceData){
                    if(s.getID() == si.getService_id()){
                        services.add(s);
                        break;
                    }
                }
            }

            List<Tags> tags = new ArrayList<>();
            List<Tags_inter> tags_inter = tagsInterData.stream()
                    .filter(t->t.getTariff_id() == tariff.getID()).toList();

            for(Tags_inter ti : tags_inter){
                for(Tags t: tagsData){
                    if(t.getID() == ti.getTag_id()){
                        tags.add(t);
                        break;
                    }
                }
            }

            List<Extra> extra = new ArrayList<>();
            List<Extra_inter> extra_inter;

            if(extraData.size() != 0){
                extra_inter = extraInterData.stream()
                        .filter(e->e.getTariff_id() == tariff.getID()).toList();
                for(Extra_inter ei : extra_inter){
                    for(Extra e : extraData){
                        if(e.getID() == ei.getExtra_id()){
                            extra.add(e);
                            break;
                        }
                    }
                }
            }

            TariffReady.TariffBuilder builder = new TariffReady.TariffBuilder(
                    tariff.getName(), tariff.getPrice(), tariff.getShortDesc()
            ).equip(equip).extra(extra).services(services).tags(tags);

            int disc = hasDiscount(tariff.getID());
            if(disc != -1){
                builder.discount(disc);
            }

            tariffReadyById.put(tariff.getID(), builder.build());
            alltariffs.add(builder.build());
        }
    }

}
