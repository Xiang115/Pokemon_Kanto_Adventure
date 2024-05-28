package pokemon_kanto_adventure;

public class Move {
    private String movename;
    private String type;
    private String category;
    private int order;
    private double atk;
    private double def;
    private double sp;
    private double dmghealratio;
    private double foe_atk;
    private double foe_def;
    private double foe_sp;
    private int power;
    private double hphealratio;
    private String description;

    public Move(String n, int lvl) {
        movename = n;
        category = library.move_cat.get(movename);
        order = library.move_order.get(movename);
        description = library.move_description.get(movename);
        if (category.equals("dmg")) {
            type = library.move_type.get(movename);
            power = library.move_dmg.get(movename).get(lvl);
        } else if (category.equals("stat")) {
            atk = library.move_stat.get(movename).get("atk");
            def = library.move_stat.get(movename).get("def");
            sp = library.move_stat.get(movename).get("sp");
            foe_atk = library.move_stat.get(movename).get("foe_atk");
            foe_def = library.move_stat.get(movename).get("foe_def");
            foe_sp = library.move_stat.get(movename).get("foe_sp");
        } else if (category.equals("heal")) {
            hphealratio = 0.5;
        } else if (category.equals("sp")) {

            power = library.move_dmg.get(movename).get(lvl);
            if (power != 0) {
                type = library.move_type.get(movename);
            }
            atk = library.move_stat.get(movename).get("atk");
            def = library.move_stat.get(movename).get("def");
            sp = library.move_stat.get(movename).get("sp");
            dmghealratio = library.move_stat.get(movename).get("healratio");
            foe_atk = library.move_stat.get(movename).get("foe_atk");
            foe_def = library.move_stat.get(movename).get("foe_def");
            foe_sp = library.move_stat.get(movename).get("foe_sp");
        }
    }

    public String getName() {
        return movename;
    }

    public int getPower() {
        return power;
    }

    public String getType() {
        return type;
    }

    public int getOrder() {
        return order;
    }

    public String getCategory() {
        return category;
    }

    public double getAtk() {
        return atk;
    }

    public double getDef() {
        return def;
    }

    public double getSp() {
        return sp;
    }

    public double getdmgheal() {
        return dmghealratio;
    }

    public double gethpheal() {
        return hphealratio;
    }

    public double getFAtk() {
        return foe_atk;
    }

    public double getFDef() {
        return foe_def;
    }

    public double getFSp() {
        return foe_sp;
    }

    public String description() {
        return description;
    }

    public void showmovdetail() {
        System.out.println("+-------------------Move Details-------------------+");
        System.out.println(movename);
        System.out.println("Category: " + category);
        System.out.println("Move order: " + order);
        if (category.equals("dmg")) {
            System.out.println("Move type: " + type);
            System.out.println("Move Power: " + power);
        } else if (category.equals("heal")) {
            System.out.println("This move will heal your pokemon for 50% of its max hp");
        } else if (category.equals("stat")) {
            if (atk != 0.0) {
                System.out.println("This move alters this pokemon's attack by " + (int) atk + " stages");
            }
            if (def != 0.0) {
                System.out.println("This move alters this pokemon's defense by " + (int) def + " stages");
            }
            if (sp != 0.0) {
                System.out.println("This move alters this pokemon's speed by " + (int) sp + " stages");
            }
            if (foe_atk != 0.0) {
                System.out.println("This move alters foe's pokemon's attack by " + (int) foe_atk + " stages");
            }
            if (foe_def != 0.0) {
                System.out.println("This move alters foe's pokemon's defense by " + (int) foe_def + " stages");
            }
            if (foe_sp != 0.0) {
                System.out.println("This move alters foe's pokemon's speed by " + (int) foe_sp + " stages");
            }
        } else if (category.equals("sp")) {
            if (power != 0) {
                System.out.println("Move type: " + type);
                System.out.println("Move Power: " + power);
            }
            if (atk != 0.0) {
                System.out.println("This move alters this pokemon's attack by " + (int) atk + " stages");
            }
            if (def != 0.0) {
                System.out.println("This move alters this pokemon's defense by " + (int) def + " stages");
            }
            if (sp != 0.0) {
                System.out.println("This move alters this pokemon's speed by " + (int) def + " stages");
            }
            if (foe_atk != 0.0) {
                System.out.println("This move alters foe's pokemon's attack by " + (int) foe_atk + " stages");
            }
            if (foe_def != 0.0) {
                System.out.println("This move alters foe's pokemon's defense by " + (int) foe_def + " stages");
            }
            if (foe_sp != 0.0) {
                System.out.println("This move alters foe's pokemon's speed by " + (int) foe_sp + " stages");
            }
            if (dmghealratio != 0.0) {
                System.out.println("This move heals your pokemon by " + dmghealratio * 100 + " % of damage dealt");
            }
        }
        System.out.println(description());
    }
}
