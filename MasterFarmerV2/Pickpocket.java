package MasterFarmer;

import MasterFarmer.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest (name = "MasterFarmer", description = "Steals stuff", properties = "author=Javier; topic=999; client =4")

public class Pickpocket extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    int startExp = 0;




    @Override
    public void start() {
        taskList.add(new Walk(ctx));
        taskList.add(new Drop(ctx));
        taskList.add(new Bank(ctx));
        taskList.add(new Heal(ctx));
        taskList.add(new Pick(ctx));

        startExp = ctx.skills.experience(Constants.SKILLS_THIEVING);

    }


    @Override
    public void poll() {
        for (Task task: taskList){
            if (task.activate()){
                task.execute();
            }
        }
    }


    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds =(milliseconds / 1000)% 60;
        long minutes =(milliseconds / (1000*60 )% 60);
        long hours =(milliseconds / (1000*60*60))% 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_THIEVING)-startExp;
        // exp left
        int currentExp = ctx.skills.experience(org.powerbot.script.rt4.Constants.SKILLS_THIEVING);
        int currLevel = ctx.skills.level(Constants.SKILLS_THIEVING);
        int expLeft = ctx.skills.experienceAt(currLevel + 1) - currentExp;

        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(138, 43, 226, 250));
        g.fillRect(3, 638, 408, 160);

        g.setColor(new Color(0, 0, 0, 250));
        g.fillRect(8, 643, 398, 150);

        g.setColor(new Color(255, 255, 255, 250));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Master Farmer  |  Javy93", 115, 665);
        g.setColor(new Color(138, 43, 226, 250));
        g.drawString("__________________________________________________", 8, 676);
        g.setColor(new Color(255, 255, 255, 250));
        g.drawString("Runtime: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 14, 695);
        g.drawString("Exp/Hour: " + (int)(expGained *(3600000D / milliseconds)), 14, 710);
        g.drawString("Exp Left: " + expLeft, 14, 725);
        g.drawString("  Thieving level: " + currLevel, 215, 695);
        g.setColor(new Color(50, 205, 50, 250));
        g.drawString("Ranarrs ", 14, 740);
        g.setColor(new Color(255, 255, 255, 250));
        g.drawString(" in inventory: " + Pick.ranarrInInventory, 69, 740);
        g.setColor(new Color(50, 205, 50, 250));
        g.drawString("Snapdragons ", 14, 755);
        g.setColor(new Color(255, 255, 255, 250));
        g.drawString(" in inventory: " + Pick.snapdragonInInventory, 106, 755);
        g.drawString("Ranarrs in bank: " + Bank.ranarrBank, 14, 770);
        g.drawString("Snapdragons in bank: " + Bank.snapdragonBank, 14, 785);
        g.setColor(new Color(255, 255, 0, 250));
        g.drawString("- Food ", 215, 740);
        g.setColor(new Color(255, 255, 255, 250));
        g.drawString("in inventory: " + Bank.foodInInventory, 265, 740);
        g.setColor(new Color(255, 255, 0, 250));
        g.drawString("- Necklaces ", 215, 755);
        g.setColor(new Color(255, 255, 255, 250));
        g.drawString("in inventory: " + Pick.necklaceInInventory, 300, 755);
        g.drawString("- Food in bank: " + Bank.foodInBank, 215, 770);
        g.drawString("- Necklaces in bank: " + Bank.necklaceBank, 215, 785);
    }


}
