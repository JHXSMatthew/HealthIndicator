import com.mcndsj.GameEvent.Events.GameEndEvent;
import com.mcndsj.GameEvent.Events.GameInitReadyEvent;
import com.mcndsj.GameEvent.Events.GameStartEvent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matthew on 27/05/2016.
 */
public class HealthIndicator extends JavaPlugin implements Listener{

    public void onEnable(){
        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onHealth(EntityDamageByEntityEvent evt){
        if(evt.getDamager() instanceof Player){
            if(evt.getEntity() instanceof Player){
                final Player damager = (Player) evt.getDamager();
                final Player entity = (Player) evt.getEntity();
                new BukkitRunnable(){
                    public void run() {
                        int current = (int) entity.getHealth();
                        int max = (int)entity.getMaxHealth();
                        sendActionBar((Player)damager, ChatColor.YELLOW + ChatColor.BOLD.toString() + entity.getName() + ": " + getColor(current,max)  + current + " / " + max);
                    }
                }.runTaskLater(this,1);

            }
        }
    }

    public String getColor(int now, int max){
        double percentage = now/max;
        if(percentage < 0.2){
            return ChatColor.RED + ChatColor.BOLD.toString();
        }else if(percentage < 0.5){
            return ChatColor.GOLD + ChatColor.BOLD.toString();
        }else{
            return ChatColor.GREEN + ChatColor.BOLD.toString();
        }
    }


    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

/*
    Set<String> healthSet = new HashSet<String>();
    BukkitTask task = null;

    public void onEnable(){
        //getServer().getPluginManager().registerEvents(this,this);
    }

    public void onDisable(){

    }

    @EventHandler
    public void onGameStart(GameStartEvent evt){
        startTask();
    }

    @EventHandler
    public void onGameEnd(GameEndEvent evt){
        cancelTask();
    }


    @EventHandler
    public void onGameInitReady(GameInitReadyEvent evt){
        cancelTask();
    }

    private void startTask(){
        if(task != null)
            return;
        for(Player p : Bukkit.getOnlinePlayers() ){
            if (p.getScoreboard() == null)
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            Scoreboard board = p.getScoreboard();
            Objective health = board.getObjective(DisplaySlot.BELOW_NAME);
            if (health == null) {
                health = board.registerNewObjective(UUID.randomUUID().toString().substring(0, 14), "dummy");
                health.setDisplayName(ChatColor.RED + "❤");
                health.setDisplaySlot(DisplaySlot.BELOW_NAME);
            }

            for(Player temp : Bukkit.getOnlinePlayers()){
                Score score = health.getScore(temp.getName());
                score.setScore((int) Bukkit.getPlayer(temp.getName()).getHealth());
            }
        }

       task =  new BukkitRunnable(){
           public void run() {
               for(Player p : Bukkit.getOnlinePlayers()) {
                   if (p.getScoreboard() == null)
                       p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                   Scoreboard board = p.getScoreboard();
                   Objective health = board.getObjective(DisplaySlot.BELOW_NAME);
                   if (health == null) {
                       health = board.registerNewObjective(UUID.randomUUID().toString().substring(0, 14), "dummy");
                       health.setDisplayName(ChatColor.RED + "❤");
                       health.setDisplaySlot(DisplaySlot.BELOW_NAME);
                   }

                   for (String key : healthSet) {
                       if (Bukkit.getPlayer(key) == null || !Bukkit.getPlayer(key).isOnline())
                           continue;
                       Score score = health.getScore(key);
                       score.setScore((int) Bukkit.getPlayer(key).getHealth());
                   }
               }
               healthSet.clear();
           }

       }.runTaskTimer(this,20,10);
    }

    private void cancelTask(){
        if(task != null){
            task.cancel();
            task = null;
        }
        healthSet.clear();
        ;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard() == null)
                return;
            Scoreboard board = p.getScoreboard();
            Objective health = board.getObjective(DisplaySlot.BELOW_NAME);
            if (health == null)
                return;
            health.unregister();
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageEvent evt){
        if(task == null){
            return;
        }
        if(evt.isCancelled())
            return;
        if (evt.getEntity() instanceof Player) {
            Player p = (Player) evt.getEntity();
            healthSet.add(p.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onReg(EntityRegainHealthEvent evt){
        if(task == null){
            return;
        }
        if(evt.isCancelled())
            return;
        if(evt.getEntity() instanceof  Player){
            Player p = (Player) evt.getEntity();
            healthSet.add(p.getName());
        }
    }
*/

}
