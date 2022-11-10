package bot.service;

public interface DataUpdateListener {
    void update(String action, long chatId);
}
