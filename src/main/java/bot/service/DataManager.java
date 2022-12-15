package bot.service;

import bot.database.entites.*;
import bot.database.repositories.*;
import bot.service.commandFactory.staff.setDiscounts.SetDiscountsCommand;
import bot.service.commandFactory.user.subscribe.SubscribeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataManager{

    //репозитории сущностей
    @Autowired
    EquipRepository equipRepository;
    List<Equip> equipData = new ArrayList<>();
    @Autowired
    ExtraRepository extraRepository;
    List<Extra> extraData = new ArrayList<>();
    @Autowired
    ServiceRepository serviceRepository;
    List<Service> serviceData = new ArrayList<>();
    @Autowired
    TagsRepository tagsRepository;
    List<Tags> tagsData = new ArrayList<>();
    @Autowired
    TariffRepository tariffRepository;
    List<Tariff> tariffsData = new ArrayList<>();
    @Autowired
    SubsRepository subsRepository;
    List<Subs> subsData = new ArrayList<>();

    //репозитории промежутков
    @Autowired
    EquipInterRepository equipInterRepository;
    List<Equip_inter> equipInterData = new ArrayList<>();
    @Autowired
    ExtraInterRepository extraInterRepository;
    List<Extra_inter> extraInterData = new ArrayList<>();
    @Autowired
    ServiceInterRepository serviceInterRepository;
    List<Service_inter> serviceInterData = new ArrayList<>();
    @Autowired
    TagsInterRepository tagsInterRepository;
    List<Tags_inter> tagsInterData = new ArrayList<>();
    @Autowired
    DiscountRepository discountRepository;
    List<Discount> discountData = new ArrayList<>();



    private static DataManager instance;
    public static DataManager getInstance(){
        if(instance == null)
            instance = new DataManager();

        return instance;
    }
    private DataManager(){
        setAllTariffs();
    }

    @Autowired
    private void loadData(){
        equipRepository.findAll().forEach(equipData::add);
        extraRepository.findAll().forEach(extraData::add);
        serviceRepository.findAll().forEach(serviceData::add);
        tagsRepository.findAll().forEach(tagsData::add);

        tariffRepository.findAll().forEach(tariffsData::add);

        equipInterRepository.findAll().forEach(equipInterData::add);
        extraInterRepository.findAll().forEach(extraInterData::add);
        serviceInterRepository.findAll().forEach(serviceInterData::add);
        tagsInterRepository.findAll().forEach(tagsInterData::add);

        discountRepository.findAll().forEach(discountData::add);

        subsRepository.findAll().forEach(subsData::add);
    }


    public boolean isSub(long chatId){
        for(Subs s:subsData){
            if(s.getID() == chatId)return true;
        }
        return false;
    }

    List<TariffReady> alltariffs = new ArrayList<>();
    public List<TariffReady> getAlltariffs() {
        return alltariffs;
    }


    public int hasDiscount(long id){
        int has = -1;
        for(Discount d: discountData){
            if(d.getTariff_id() == id){
                has = d.getPrice();
                break;
            }
        }

        return has;
    }

    @Autowired
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
                    tariff.getName(), tariff.getPrice(), tariff.getShortDesc(), tariff.getID()
            ).equip(equip).extra(extra).services(services).tags(tags);

            int disc = hasDiscount(tariff.getID());
            if(disc != -1){
                builder.discount(disc);
            }

            alltariffs.add(builder.build());
        }
    }

    public void reSub(String action, long chatId){
        Subs s;
        switch (action){
            case "subscribe":
                subsData = new ArrayList<>();
                s = new Subs();
                s.setID(chatId);
                subsRepository.save(s);
                subsRepository.findAll().forEach(subsData::add);
                break;

            case "unsubscribe":
                subsData = new ArrayList<>();
                s = new Subs();
                s.setID(chatId);
                subsRepository.delete(s);
                subsRepository.findAll().forEach(subsData::add);
                break;
        }
    }

    public void createDiscount(int id, int price){
        Discount d = new Discount();
        d.setPrice(price);
        d.setTariff_id(id);
        discountRepository.save(d);
        discountData = new ArrayList<>();
        discountRepository.findAll().forEach(discountData::add);
    }

    public void deleteDiscount(long id){
        Discount d = new Discount();
        discountRepository.deleteById(id);
        discountData = new ArrayList<>();
        discountRepository.findAll().forEach(discountData::add);
    }


}
