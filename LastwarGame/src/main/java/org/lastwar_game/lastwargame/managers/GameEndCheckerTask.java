package org.lastwar_game.lastwargame.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.example.gamelogic.lastwargamelogic.flag.CoreFlagDetectorTask;
import org.lastwar_game.lastwargame.managers.GameManager;

public class GameEndCheckerTask extends BukkitRunnable {

    private final String worldName;
    private final JavaPlugin plugin;
    private boolean overtimeStarted = false;

    public GameEndCheckerTask(String worldName, JavaPlugin plugin) {
        this.worldName = worldName;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective(worldName);
        if (objective == null) return;

        int redScore = objective.getScore("RED").getScore();
        int blueScore = objective.getScore("BLUE").getScore();

        // ✅ Проверка на игроков
        long activePlayers = world.getPlayers().stream().filter(p -> !p.isDead()).count();
        if (activePlayers < 2) {
            Bukkit.broadcastMessage("§cNot enough players. Game is ending...");
            GameManager.scheduleFinalEnd(world,plugin,5);
            this.cancel();
            return;
        }
    }
}
