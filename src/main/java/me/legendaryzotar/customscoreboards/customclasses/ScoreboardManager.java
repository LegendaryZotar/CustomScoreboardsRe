package me.legendaryzotar.customscoreboards.customclasses;

import me.clip.placeholderapi.PlaceholderAPI;
import me.legendaryzotar.autosellmode.AutoSellMode;
import me.legendaryzotar.customscoreboards.CustomScoreboards;
import me.legendaryzotar.customscoreboards.other.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bukkit.Bukkit.getServer;

public class ScoreboardManager {

    private static ScoreboardManager _instance = null;

    public ScoreboardManager() {
        this.plugin = CustomScoreboards.getInstance();
        Setup();
    }

    public static List<Player> players = Collections.synchronizedList(new ArrayList<>());

    public static ScoreboardManager Instance() {
        if (_instance == null) {
            _instance = new ScoreboardManager();
        }
        return _instance;
    }

    private static CustomScoreboards plugin = null;

    public static ConcurrentHashMap<UUID, LocalDateTime> LastUsed = new ConcurrentHashMap<>();

    public static List<Player> QueuedUpdates = Collections.synchronizedList(new ArrayList<>());

    static List<String> colorDex = Collections.synchronizedList(new ArrayList<String>() {{
        add(Utils.Format("&k" + "&0"));
        add(Utils.Format("&k" + "&1"));
        add(Utils.Format("&k" + "&2"));
        add(Utils.Format("&k" + "&3"));
        add(Utils.Format("&k" + "&4"));
        add(Utils.Format("&k" + "&5"));
        add(Utils.Format("&k" + "&6"));
        add(Utils.Format("&k" + "&7"));
        add(Utils.Format("&k" + "&8"));
        add(Utils.Format("&k" + "&9"));
        add(Utils.Format("&k" + "&a"));
        add(Utils.Format("&k" + "&b"));
        add(Utils.Format("&k" + "&c"));
        add(Utils.Format("&k" + "&d"));
        add(Utils.Format("&k" + "&e"));
        add(Utils.Format("&k" + "&f"));
        add(Utils.Format("&k" + "&r"));
        add(Utils.Format("&k" + "&l"));
        add(Utils.Format("&k" + "&o"));
        add(Utils.Format("&k" + "&n"));
        add(Utils.Format("&k" + "&m"));
        add(Utils.Format("&k" + "&k"));
    }});

    static AtomicInteger Delay = new AtomicInteger(10);

    static List<String> scores = Collections.synchronizedList(new ArrayList<>());
    static String title = "";
    static int PlayersOnline = 0;
    static int MaxPlayers = 0;
    static String ASOn = "";
    static String ASOff = "";
    static boolean AutoSell = false;
    public static int minDelay = 250;

    public static void createScoreboard(Player p, boolean updateAfter) {
        Scoreboard board = getServer().getScoreboardManager().getNewScoreboard();
        String playerName = p.getName();
        boolean isOn = false;
        if (AutoSell)
            isOn = AutoSellIsOn(p);
        boolean finalIsOn = isOn;

        Bukkit.getScheduler().runTaskAsynchronously(CustomScoreboards.getInstance(), () -> {
            Objective obj = board.registerNewObjective("PlayerSB", "Dummy", title);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.setDisplayName(Utils.Format(title));
            String playersOnline = PlayersOnline + "";
            String maxPlayers = MaxPlayers + "";

            for (String rawScore : scores) {
                String fScore = rawScore;
                if (AutoSell) {
                    if (finalIsOn) {
                        fScore = fScore.replace("%ASMODE%", ASOn);
                    } else
                        fScore = fScore.replace("%ASMODE%", ASOff);
                }
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    try {
                        fScore = PlaceholderAPI.setPlaceholders(p, fScore);
                    } catch (Exception e) {
                        if (!(e instanceof NullPointerException)) {
                            e.printStackTrace();
                        }
                    }
                }
                int index = scores.indexOf(rawScore);
                Team cur = board.registerNewTeam(index + "");
                cur.addEntry(colorDex.get(index));
                cur.setPrefix(Utils.Format(fScore));
                Score temp = obj.getScore(colorDex.get(index));
                temp.setScore(index);
            }
            getServer().getScheduler().scheduleSyncDelayedTask(CustomScoreboards.getInstance(), () -> {
                p.setScoreboard(board);
                LastUsed.put(p.getUniqueId(), LocalDateTime.now());
                if (updateAfter)
                    scoreBoardUpdater();
            }, 1L);
            ScoreboardManager.players.add(p);
        });
    }

    static void QueueUpdate(Player p) {
        if (!QueuedUpdates.contains(p)) {
            LocalDateTime lastUsed = LastUsed.get(p.getUniqueId());
            QueuedUpdates.add(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(CustomScoreboards.getInstance(), () -> {
                if (QueuedUpdates.contains(p)) {
                    UpdateScoreboard(p, true);
                    QueuedUpdates.remove(p);
                }
            }, Duration.between(lastUsed, lastUsed.plus(minDelay, ChronoUnit.MILLIS)).toMillis() / 50L);

        }
    }

    public static void UpdateScoreboard(Player p, boolean queued) {
        UUID id = p.getUniqueId();
        if (LastUsed.containsKey(p.getUniqueId())) {
            if (!queued) {
                if (!(Duration.between(LastUsed.get(id), LocalDateTime.now()).toMillis() >= minDelay)) {
                    QueueUpdate(p);
                    return;
                }
            }
        }

        Scoreboard board = p.getScoreboard();
        boolean isOn = false;
        if (AutoSell)
            isOn = AutoSellIsOn(p);
        boolean finalIsOn = isOn;

        Bukkit.getScheduler().runTaskAsynchronously(CustomScoreboards.getInstance(), () -> {
            for (String rawScore : scores) {
                String fScore = rawScore;
                if (AutoSell) {
                    if (finalIsOn) {
                        fScore = fScore.replace("%ASMODE%", ASOn);
                    } else
                        fScore = fScore.replace("%ASMODE%", ASOff);
                }
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    try {
                        fScore = PlaceholderAPI.setPlaceholders(p, fScore);
                    } catch (Exception e) {
                        if (!(e instanceof NullPointerException)) {
                            e.printStackTrace();
                        }
                    }
                }
                int index = scores.indexOf(rawScore);
                board.getTeam(index + "").setPrefix(Utils.Format(fScore));
                LastUsed.put(id, LocalDateTime.now());
            }
        });
    }



    static void SetAutoSellOn(String text) {
        ASOn = text;
    }

    static void SetAutoSellOff(String text) {
        ASOff = text;
    }

    static boolean AutoSellIsOn(Player p) {
        return AutoSellMode.getApi().isOn(p);
    }



    public static void Setup() {
        players.clear();
        LastUsed.clear();
        QueuedUpdates.clear();
        List<String> lScores = Collections.synchronizedList(plugin.getConfig().getStringList("Scoreboard.Items"));
        Collections.reverse(lScores);
        SetAutoSellOn(CustomScoreboards.getInstance().getConfig().getString("ASONMODE"));
        SetAutoSellOff(CustomScoreboards.getInstance().getConfig().getString("ASOFFMODE"));
        Delay = new AtomicInteger(CustomScoreboards.getInstance().getConfig().getInt("StartUpDelay"));
        title = CustomScoreboards.getInstance().getConfig().getString("Scoreboard.Title");
        AutoSell = (getServer().getPluginManager().getPlugin("AutoSellMode") != null);
        minDelay = CustomScoreboards.getInstance().getConfig().getInt("MinUpdateDelay");
        scores = lScores;

        Bukkit.getScheduler().scheduleSyncDelayedTask(CustomScoreboards.getInstance(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                createScoreboard(p, false);
            }
        }, Delay.get() * 20L);
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        Setup();
    }

    public static void scoreBoardUpdater() {
        for (Player p : players) {
            UpdateScoreboard(p, false);
        }
    }
}