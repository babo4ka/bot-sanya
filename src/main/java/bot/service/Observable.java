package bot.service;

public interface Observable {
    void addObserver(DataUpdateListener listener);
    void removeObserver(DataUpdateListener listener);
    void notifyObservers(String action, long chatId);
}
