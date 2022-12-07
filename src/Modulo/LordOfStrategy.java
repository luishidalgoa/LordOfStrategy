import java.lang.reflect.Array;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.LineEvent;

import org.w3c.dom.Attr;
public class LordOfStrategy{
    static int[]flag={0,0}; //este flag se encargara de evitar que un evento fijo se repita 2 veces. flag [0]=0 significara que el primer evento fijo no ha sucedido. si es =1 entonces habra sucedido
    /*
     * Int[]valores={xp,xpIA,tablero,turno,oro,oroIA}
     *                0   1     2      3    4    5   
     * Int[]memoria={lastOro,sumOro,restOro,timeDeuda,otros,lastOroIA,sumOroIA,restOroIA} //almacena el resultado del los ingresos del pasado turno, ademas de los ingresos, gastos del pasado turno, tiempo en deuda y otros ingresos
     *                  0       1       2       3       4       5        6          7   
     * int[]guerra={lvlAttackIA,lvlDefIA,lvlAttackPlayer,lvlDefPlayer} [4]=WinPlayer [5]=WinEnemigo
     *                  0           1          2               3    
     * int[]mejoras={granjas,pesca,minas,petroleras,aserrerias,fabricasAlimentarias,fabricasBienes,fabricasMilitares, LanzaMisiles,antiMisiles}
     *                 0       1    2        3          4               5                  6               7                8           9
     * int[]mejorasIA={granjas,pesca,minas,petroleras,aserrerias,fabricasAlimentarias,fabricasBienes,fabricasMilitares, LanzaMisiles,antiMisiles}
     *                    0       1    2        3          4               5                  6               7                8           9
    */
    static int[]valores={0,0,70,0,500,500};
    static int[]memoria={0,0,0,0,0,0,0,0};
    static int[]guerra={1,1,1,1,0,0};
    static int[]mejoras={0,0,0,0,0,0,0,0,0,0};
    static int[]mejorasIA={0,0,0,0,0,0,0,0,0,0};
    static boolean exit=false;
    public static void main(String[]args) {
        Scanner sc=new Scanner(System.in);
        String player=leeNombre();
        System.out.println("________________________________________________");
        System.out.println("Escriba help. Para mas ayuda");
        System.out.println("________________________________________________");
        for(;valores[3]<100 && exit==false;){ 
            memoria[4]=0;
            jugador(player);
        }
    }
    public static void jugador(String player){
        //--------------------Comandos----------------
        evento();
        boolean endTurno=false;
        for(;endTurno==false && exit==false;){
            String comando=leeComando("Introduce comando");
            if(comando.equals("HELP")){
                help();
            }else if(comando.equals("RULES")){
                Rules();
            }else if(comando.equals("BATALLA")){
                batalla(player);
                System.out.println("________________________________________________");
                endTurno=true;
            }else if(comando.equals("XP")){
                System.out.println("Tienes "+valores[0]+" de Experiencia");miniPausa();
                System.out.println("________________________________________________");
            }else if(comando.equals("ORO")){
                System.out.println("tienes "+valores[4]+" de oro en las arcas");
            }else if(comando.equals("MEJORAS")){
                endTurno=mejoras();
            }else if(comando.equals("MILITAR")){
                militar();
            }else if(comando.equals("LVLUPATTACK")){
                int[]array=lvlUpAttack(valores[0],guerra[2]);
                valores[0]=array[0];
                guerra[2]=array[1];
                int finalTurno=array[2];
                if(finalTurno==0){
                    endTurno=false;
                }else{
                    System.out.println("El nivel de ataque actual es "+guerra[2]+" Te queda "+valores[0]+" de xp");miniPausa();
                    endTurno=true;
                }
                System.out.println();miniPausa();
                System.out.println("________________________________________________");
            }else if(comando.equals("LVLUPDEFENSA")){
                int[]array=lvlUpDefensa(valores[0],guerra[3]);
                valores[0]=array[0];
                guerra[3]=array[1];
                int finalTurno=array[2];
                if(finalTurno==0){
                    endTurno=false;
                }else{
                    System.out.println("El nivel de defensa actual es "+guerra[3]+" Te queda "+valores[0]+" de xp");miniPausa();
                    endTurno=true;
                }
                System.out.println();miniPausa();
                System.out.println("________________________________________________");
            }else if(comando.equals("CLEAR")){
                clear();miniPausa();
                System.out.println("________________________________________________");
                endTurno=false;
            }else if(comando.equals("CREDITOS")){
                creditos();
                endTurno=false;
            }else if(comando.equals("EXIT")){
                clear();miniPausa();
                System.out.println("________________________________________________");
                exit=true;
            }
            if(comando.equals("SALTAR") || endTurno==true){
                turnoIA(player); //esta operacion enviara todo lo necesario para que se desarrolle el turno de la IA. y vendra de regreso en forma de array
                endTurno=true;
                balance();
                System.out.println("\u001B[0m");
                if(valores[3]==100){
                    System.out.println("Fin partida");
                    creditos();
                }
            }
        }
    }
    public static void balance(){
        //aqui almacenamos los beneficios y gastos por turno
        memoria[1]=(mejoras[0]*30)+(mejoras[1]*40)+(mejoras[2]*80)+(mejoras[3]*100)+(mejoras[4]*30)+(mejoras[5]*60)+(mejoras[6]*100)+memoria[4];
        if(((mejoras[0]*20)+(mejoras[1]*35)-(mejoras[5]*43))<0){
            memoria[1]-=mejoras[5]*60;
        }
        if(((mejoras[2]*30)+(mejoras[4]*50)-(mejoras[6]*43))<0){
            memoria[1]-=mejoras[6]*100;
        }
        memoria[2]=(mejoras[0]*5)+(mejoras[1]*7)+(mejoras[2]*23)+(mejoras[3]*40)+(mejoras[4]*12)+(mejoras[5]*20)+(mejoras[6]*25)+(mejoras[7]*50)+(mejoras[8]*50)+(mejoras[9]*40);
        valores[4]+=memoria[1]-memoria[2];
        memoria[0]=memoria[1]-memoria[2];
        memoria[6]=(mejorasIA[0]*30)+(mejorasIA[1]*40)+(mejorasIA[2]*80)+(mejorasIA[3]*100)+(mejorasIA[4]*30)+(mejorasIA[5]*60)+(mejorasIA[6]*100);
        if(((mejorasIA[0]*20)+(mejorasIA[1]*35)-(mejorasIA[5]*43))<0){
            memoria[6]-=mejorasIA[5]*60;
        }
        if(((mejorasIA[2]*30)+(mejorasIA[4]*50)-(mejorasIA[6]*43))<0){
            memoria[6]-=mejorasIA[6]*100;
        }
        memoria[7]=(mejorasIA[0]*5)+(mejorasIA[1]*7)+(mejorasIA[2]*23)+(mejorasIA[3]*40)+(mejorasIA[4]*12)+(mejorasIA[5]*20)+(mejorasIA[6]*25)+(mejorasIA[7]*50)+(mejorasIA[8]*50)+(mejorasIA[9]*40);
        valores[5]+=memoria[6]-memoria[7];
        memoria[5]=memoria[6]-memoria[7];
        if(valores[4]>0){
            memoria[3]=0;
        }else if(valores[4]<=0){
            memoria[3]++;
        }

    }
    //----------------------------------------------- FUNCIONES IA--------------------------     
    public static void turnoIA(String player){
        System.out.println();miniPausa();
        System.out.println("\033[34m"+"Turno IA");miniPausa();
        int result=ataquelvl(guerra[2]); 
        boolean es=false;
        boolean endTurno=false;
        boolean ia=true;
        //__________________________Mejoras____________
            if(memoria[5]<=0 && valores[3]!=0){
                endTurno=mejoraIA(1);
            }else if(memoria[5]<20 && memoria[5]>0){
                int random=(int)(Math.random()*3);
                if(random<2){
                    endTurno=mejoraIA(2);
                }
            }else if (memoria[5]<=100 && memoria[5]>20){
                int random=(int)(Math.random()*2);
                if(random==1){
                    endTurno=mejoraIA(3);
                }
            }else if (memoria[5]>100){
                int random=(int)(Math.random()*3);
                if(random==1){
                    endTurno=mejoraIA(4);
                }
            }
         //___________________________Subir nivel_________
        if((valores[1]>=nivel(guerra[1]+1, es=true,ia)) || ((valores[0]>=nivel(guerra[0]+1,es=false,ia)) && endTurno==false)){
            boolean isUpdate=false;
            if(valores[0]>=nivel(guerra[0]+1,es=false,ia) && valores[1]>=nivel(guerra[1]+1,es=false,ia) && endTurno==false){
                int random=(int)(Math.random()*3);
                if(random==0){
                    if(guerra[0]<15){
                        valores[1]-=nivel(guerra[0]+1, es,ia);
                        guerra[0]++;
                        endTurno=true;
                        isUpdate=true;
                    }
                }else if(random==1){
                    if(guerra[1]<15){
                        valores[1]-=nivel(guerra[1]+1, es,ia);
                        guerra[1]++;
                        endTurno=true;
                        isUpdate=true;
                    }
                }
            }else{
                int random=(int)(Math.random()*2);
                if((random==0) && valores[1]>=nivel(guerra[0]+1, es=false,ia) && endTurno==false && isUpdate==false && guerra[0]<15){
                        valores[1]-=nivel(guerra[0]+1, es,ia);
                        guerra[0]++;
                        endTurno=true;
                }
                if((random==1) && valores[1]>=nivel(guerra[1]+1, es=false,ia) && endTurno==false && isUpdate==false && guerra[1]<15){
                        valores[1]-=nivel(guerra[1]+1, es,ia);
                        guerra[1]++;
                        endTurno=true;
                }
            }
            
        } 
        // ATAQUE IA
        if((ataquelvl(guerra[1])>=(result-=result*30/100)) &&endTurno==false){
            int dado=0+(int)(Math.random()*4);
            if(dado<4){
               batalla(player);
            }
        }
        //texto_______________
        System.out.println("\033[34mxp jugador "+valores[0]);miniPausa();
        System.out.println("xp Enemigo "+valores[1]);miniPausa();
        System.out.println("El enemigo tiene "+valores[2]+"/100 zonas del tablero");miniPausa();
        System.out.println("nivel ataque Enemigo "+guerra[0]);miniPausa();
        System.out.println("nivel defensa Enemigo "+guerra[1]);miniPausa();
        System.out.println("Oro del enemigo "+ valores[5]);
        pausa();
        System.out.println("turno "+(++valores[3])+" Finalizado");;miniPausa();
        System.out.println("___________________________________________________________");miniPausa();
    }
    public static boolean mejoraIA(int a){
        int[]costes={200,300,500,800,400,600,600,400,150,100};
        boolean isTrue=false;
        if(a==1 && isTrue==false){
            for(int i=0;i<4 && isTrue==false;i++){
                a=(int)(Math.random()*6);
                if(valores[5]>=costes[a]){
                    valores[5]-=costes[a];
                    mejorasIA[a]++;
                    isTrue=true;
                }
            }
        }else if(a==2 && isTrue==false){
            a=(int)(Math.random()*2);
            if(a==0){
                for(int i=0;i<4 && isTrue==false;i++){
                    a=(int)(Math.random()*6);
                    if(valores[5]>=costes[a]){
                        valores[5]-=costes[a];
                        mejorasIA[a]++;
                        isTrue=true;
                    }
                }
                
            }else{
                for(int i=0;i<6 && isTrue==false;i++){
                    a=(int)(Math.random()*9);
                    if(valores[5]>=costes[a]){
                        valores[5]-=costes[a];
                        mejorasIA[a]++;
                        isTrue=true;
                    }
                }
            }
        }else if(a==3 && isTrue==false){
            for(int i=0;i<6 && isTrue==false;i++){
                a=(int)(Math.random()*9);
                if(valores[5]>=costes[a]){
                    valores[5]-=costes[a];
                    mejorasIA[a]++;
                    isTrue=true;
                }
            }
        }else if(a==4 && isTrue==false){
            for(int i=0;i<6 && isTrue==false;i++){
                a=(int)(Math.random()*9);
                if(valores[5]>=costes[a]){
                    valores[5]-=costes[a];
                    mejorasIA[a]++;
                    isTrue=true;
                }
            }
        }
        return isTrue;
    }
//----------------------------------------------Niveles----------------------------------------
    public static int ataquelvl(int lvl){
        int[]ataque=new int[16]; //15 niveles de ataque
                ataque[1]=(int)(Math.random()*11)+1;
                ataque[2]=(int)(Math.random()*(14-7))+7;
                ataque[3]=(int)(Math.random()*(24-10))+10;
                ataque[4]=(int)(Math.random()*(31-20))+20;
                ataque[5]=(int)(Math.random()*(35-27))+27;
                ataque[6]=(int)(Math.random()*(38-30))+30;
                ataque[7]=(int)(Math.random()*(47-34))+34;
                ataque[8]=(int)(Math.random()*(56-43))+43;
                ataque[9]=(int)(Math.random()*(80-67))+67;
                ataque[10]=(int)(Math.random()*(100-77))+77; //los rangos son mas inestables
                ataque[11]=(int)(Math.random()*(120-80))+80;
                ataque[12]=(int)(Math.random()*(128-100))+100;
                ataque[13]=(int)(Math.random()*(140-110))+110;
                ataque[14]=(int)(Math.random()*(148-130))+130;
                ataque[15]=(int)(Math.random()*(170-140))+140;
                return ataque[lvl];
    }
    public static int defensalvl(int lvl){
        int[]defensa=new int[16];//15 niveles de defensa
                defensa[1]=(int)(Math.random()*(41-20))+20;
                defensa[2]=(int)(Math.random()*(81-36))+36;
                defensa[3]=(int)(Math.random()*(101-74))+74;
                defensa[4]=(int)(Math.random()*(116-81))+81;
                defensa[5]=(int)(Math.random()*(151-95))+95;
                defensa[6]=(int)(Math.random()*(160-120))+120;
                defensa[7]=(int)(Math.random()*(181-136))+136;
                defensa[8]=(int)(Math.random()*(201-174))+174;
                defensa[9]=(int)(Math.random()*(216-181))+181;
                defensa[10]=(int)(Math.random()*(241-195))+195;
                defensa[11]=(int)(Math.random()*(260-220))+220;
                defensa[12]=(int)(Math.random()*(281-236))+236;
                defensa[13]=(int)(Math.random()*(301-274))+274;
                defensa[14]=(int)(Math.random()*(316-281))+281;
                defensa[15]=(int)(Math.random()*(351-295))+295;
                return defensa[lvl];
    }
    public static int nivel(int lvl,boolean es, boolean ia){   //si el boolean es false=ataque. Si es True=defensa
        int xp=0;
        boolean ocurrio=false;
        if(es==false){//ataque
            System.out.print("\033[33m");
            if(lvl==2){
                xp=1000;
                if(ia==false){
                System.out.println("Cuesta 1000 xp");
                }
            }else if(lvl==3){
                xp=1200;
                if(ia==false){
                    System.out.println("Cuesta 1200 xp");}
            }else if(lvl==4){
                xp=1400;
                if(ia==false){
                    System.out.println("Cuesta 1400 xp");}
            }else if(lvl==5){
                xp=1700;
                if(ia==false){
                    System.out.println("Cuesta 1700 xp");}
            }else if(lvl==6){
                xp=2000;
                if(ia==false){
                    System.out.println("Cuesta 2000 xp");}
            }else if(lvl==7){
                xp=2100;
                if(ia==false){
                    System.out.println("Cuesta 2100 xp");}
            }else if(lvl==8){
                xp=2200;
                if(ia==false){
                    System.out.println("Cuesta 2200 xp");}
            }else if(lvl==9){
                xp=2300;
                if(ia==false){
                    System.out.println("Cuesta 2300 xp");}
            }else if(lvl==10){
                xp=2500;
                if(ia==false){
                    System.out.println("Cuesta 2500 xp");}
            }else if(lvl==11){
                xp=2600;
                if(ia==false){
                    System.out.println("Cuesta 2600 xp");}
            }else if(lvl==12){
                xp=2700;
                if(ia==false){
                    System.out.println("Cuesta 2700 xp");}
            }else if(lvl==13){
                xp=2800;
                if(ia==false){
                    System.out.println("Cuesta 2800 xp");}
            }else if(lvl==14){
                xp=2900;
                if(ia==false){
                    System.out.println("Cuesta 2900 xp");}
            }else if(lvl==15){
                xp=3000;
                if(ia==false){
                    System.out.println("Cuesta 3000 xp");}
            }else{
                xp=999999999;
            }
            System.out.print("\u001B[0m");
        }
        if(es==true){
            System.out.println("\033[33m");
            if(lvl==2){
                xp=1000;
                if(ia==false){
                    System.out.println("Cuesta 1000 xp");
                }
            }else if(lvl==3){
                xp=1200;
                if(ia==false){
                    System.out.println("Cuesta 1200 xp");}
            }else if(lvl==4){
                xp=1400;
                if(ia==false){
                    System.out.println("Cuesta 1400 xp");}
            }else if(lvl==5){
                xp=1700;
                if(ia==false){
                    System.out.println("Cuesta 1700 xp");}
            }else if(lvl==6){
                xp=2000;
                if(ia==false){
                    System.out.println("Cuesta 2000 xp");}
            }else if(lvl==7){
                xp=2100;
                if(ia==false){
                    System.out.println("Cuesta 2100 xp");}
            }else if(lvl==8){
                xp=2200;
                if(ia==false){
                    System.out.println("Cuesta 2200 xp");}
            }else if(lvl==9){
                xp=2300;
                if(ia==false){
                    System.out.println("Cuesta 2300 xp");}
            }else if(lvl==10){
                xp=2500;
                if(ia==false){
                    System.out.println("Cuesta 2500 xp");}
            }else if(lvl==11){
                xp=2600;
                if(ia==false){
                    System.out.println("Cuesta 2600 xp");}
            }else if(lvl==12){
                xp=2700;
                if(ia==false){
                    System.out.println("Cuesta 2700 xp");}
            }else if(lvl==13){
                xp=2800;
                if(ia==false){
                    System.out.println("Cuesta 2800 xp");}
            }else if(lvl==14){
                xp=2900;
                if(ia==false){
                    System.out.println("Cuesta 2900 xp");}
            }else if(lvl==15){
                xp=3000;
                if(ia==false){
                    System.out.println("Cuesta 3000 xp");}
            }else{
                xp=999999999;
            }
        }
        System.out.print("\u001B[0m");
        return xp;
    }
    public static void tablero(){
        System.out.println("posees "+(100-valores[2])+"/100 zonas del tablero");
    }
    public static void evento(){
        Scanner sc=new Scanner(System.in);
        //_________________eventos fijos______________
            //------evento 1 La IA se hace con terrenos ilegales
        if (valores[3]>4 && valores[3]<9 && flag[0]==0){  //evento fijo 0
            int random=(int)(Math.random()*2);
            int escribe=-1;
            boolean isTrue=false;
            if(random==1 || valores[3]==8){
                escribir("\033[36mViolacion territorial"); miniPausa();System.out.println();
                escribir("Sr. El enemigo a decidido violar nuestra soberania"); miniPausa();System.out.println();
                escribir("y se ha anexionado "+(random=1+(int)(Math.random()*4))+" zonas del tablero"); miniPausa();System.out.println();
                escribir("Esto no puede quedar impune."); miniPausa();System.out.println();
                escribir("________________________"); miniPausa();miniPausa();System.out.println();
                escribir("\033[33m(Escribir 1). No hacer nada. (el enemigo se anexionara "+random+" zonas del tablero)"); miniPausa();System.out.println();
                if(valores[0]>199){
                    escribir("(Escribir 2). Esto no quedara impune. (el enemigo se anexionara) "+((int)(random/2))+" zonas del tablero, pero perderemos "+(valores[0]*15)/100+" xp");miniPausa();System.out.println();
                    isTrue=true;//flag para conocer si se funple la regla de que xp sea mayor a 199
                    System.out.println();
                }
                System.out.print("\u001B[0m");
                for(int i=0;i==0;){
                    escribe=leeEntero("¿Que desea hacer?");miniPausa();miniPausa();
                    if(escribe==1){
                        valores[2]+=random;
                        i++;
                    }else if(escribe==2 && isTrue==true){
                        random/=2;
                        valores[0]-=(valores[0]*10)/100;
                        valores[2]+=random;
                        i++;
                    }else{
                        System.out.println("\033[31mSolo puedes escoger entre las opciones disponibles\u001B[0m");miniPausa();
                        i=0;
                    }

                } 
                flag[0]=1;
            }
        }
        if (valores[3]>17 && valores[3]<25 && flag[1]==0){  // evento fijo 1
            int random=(int)(Math.random()*3);
            int escribe=-1;
            boolean isTrue=false;
            if(random==1 || valores[3]==24){
                escribir("\033[36mSr. Nuestros enemigos nos hacen una generosa propuesta.");miniPausa();System.out.println();
                if((guerra[2]+guerra[3])>=(guerra[0]+guerra[1])){
                    escribir("Ofrecen una rendicion generosa ellos estan dispuestos a");miniPausa();System.out.println();
                    escribir("ofrecernos reparaciones de guerra lo que supone "+(valores[5]*(10+(guerra[4]*2))/100)+" de oro");miniPausa();System.out.println();
                    escribir("A cambio nos piden una tregua de 20 turnos");miniPausa();System.out.println();
                    escribir("________________________"); miniPausa();miniPausa();System.out.println();
                    escribir("\033[33m(Escribir 1). Es una oferta generosa. Debemos aceptarla"); miniPausa();System.out.println();
                    escribir("(Escrobor 2). Es una oferta Generosa. Pero no es de nuestro interes");miniPausa();System.out.println();
                    escribir("(Escribir 3). ¿Pero que burla es esta?\u001B[0m"); miniPausa();System.out.println();
                    for(int i=0;i==0;){
                        escribe=leeEntero("¿Que desea hacer?");miniPausa();miniPausa();
                        if(escribe==1){
                            valores[4]+=(valores[5]*(10+(guerra[4]*2))/100);
                            guerra[5]=20;
                            i++;
                        }else if(escribe==2){
                            i++;
                        }else if(escribe==3){
                            i++;
                        }else{
                            System.out.println("Solo puedes escoger entre las opciones disponibles");miniPausa();
                            i=0;
                        }
                    }   
                }else{
                    System.out.println("Nos ofrecen una paz condicional de 20 turnos");miniPausa();
                    System.out.println("Si aceptamos sus exigencias: "+ (valores[9]*10)/100);miniPausa();
                }
            }
        }
        //_________________eventos dinamicos______________
        int dinamico=(int)(Math.random()*16);
        if(dinamico==5 && valores[3]>3){
            dinamico=(int)(Math.random()*2);
            if(dinamico==0){ //primer evento dinamico
                int escribe=-1;
                System.out.println("Buenas noticias Sr.");miniPausa();
                System.out.println("Los entrenamientos han sido un exito."); miniPausa();
                System.out.println("Nuestros escuadrones han demostrado tener experiencia"); miniPausa();
                System.out.println("________________________"); miniPausa();miniPausa();
                if(valores[0]<1000){
                    System.out.println("(Escribir 1). Recibimos "+(valores[0]*10)/100+" de xp");miniPausa();
                }else{
                    System.out.println("(Escribir 1). Recibimos 100 de xp");miniPausa();
                }
                for(int i=0;i==0;){
                    escribe=leeEntero("¿Que desea hacer?");miniPausa();miniPausa();
                    if(escribe==1 && valores[0]<1000){
                        valores[0]+=(valores[0]*10)/100;
                        i++;
                    }else if (valores[0]>999){
                        valores[0]+=100;
                        i++;
                    }else{
                        System.out.println("Solo puedes escoger entre las opciones disponibles");
                        i=0;
                    }
                }
                if(dinamico==1){ //segundo evento dinamico
                    
                }
            }
        }
    }

    //-----------------------------------LEE ----------------------------------
    public static int leeEntero(String msn){
        Scanner sc=new Scanner(System.in);
        int numero=0;
        try{
            System.out.println(msn);
            numero=sc.nextInt();
        }catch (Exception e){
            System.out.println("Introduce un numero valido");
        }
        return numero;
    }
    public static String leeNombre(){
        Scanner sc=new Scanner(System.in);
        String nombre="";
        try{
            System.out.println("Introduce Nombre jugador");
            nombre=sc.nextLine();

        }catch (Exception e){
            System.out.println("Introduce una cadena de caracteres");
        }
        return nombre;
    }
    public static String leeComando(String msn){
        Scanner sc=new Scanner(System.in);
        String evento="";
        try{
            System.out.println(msn);
            evento=sc.nextLine();
            evento=evento.toUpperCase();

        }catch (Exception e){
            System.out.println("Introduce un evento");
        }
        return evento;
    }
    public static boolean confirmar(String msn){
        boolean confirm=false;
        Scanner sc=new Scanner(System.in);
        String respuesta;
        int i=0;
        try{
            do{
                System.out.println("\033[32m");
                System.out.println(msn);
                System.out.println("¿Estas seguro?");
                System.out.println("1. Si: Aceptaras");
                System.out.println("2. No: declinaras");
                System.out.println("\u001B[0m");
                respuesta=sc.nextLine();
                respuesta=respuesta.toUpperCase();
                if(respuesta.equals("SI")){
                    confirm=true;
                    i++;
                }else if(respuesta.equals("NO")){
                    confirm=false;
                    i++;
                }else{
                    System.out.println("\033[32m");
                    System.out.println("Escribe una opcion valida");
                    System.out.println("\u001B[0m");
                }
            }while(i==0);
        }catch (ExceptionInInitializerError e){
            System.out.println("Escribe una opcion valida");
        }
        return confirm;
    }
    //------------------------------------------------Comandos-----------------
    public static void help(){
        System.out.println("\033[31m");
        System.out.println("_______________________Lista comandos________________");miniPausa();
        System.out.println("1. help : Muestra todos lo comandos del juego");miniPausa();
        System.out.println("2. Rules: Muestra las reglas de juego y como funciona");miniPausa();
        System.out.println("3. batalla: Aceptas ir al ataque contra el enemigo (saltaras turno)");miniPausa();
        System.out.println("4. xp: Muestra el total XP acumulado");miniPausa();
        System.out.println("5. oro: Muestra el oro que tienes y el balance de beneficios");miniPausa();
        System.out.println("6. Mejoras: Muestra un submenu de mejoras de edificios");miniPausa(); //no habilitado
        System.out.println("7. Militar: Muestra un submenu de informacion detallada de guerra");miniPausa();
        System.out.println("8. lvlUpAttack: Consumes XP para subir el nivel de ataque (saltaras turno)");miniPausa();
        System.out.println("9. lvlUpDefensa: Consumes XP para subir el nivel de defensa (saltaras turno)");miniPausa();
        System.out.println("10. clear: Limpia el chat");miniPausa();
        System.out.println("11. Saltar: Cedes tu turno a la IA");miniPausa();
        System.out.println("12. exit: salir del programa");miniPausa();
        System.out.println("13. creditos: Creditos del juego");miniPausa();
        System.out.println("________________________________________________________");
        System.out.println("\u001B[0m");
    }
    public static void Rules(){
        System.out.println("\033[31m");
        System.out.println("Lord Of Strategy es un juego de rol y estrategia");miniPausa();
        System.out.println("El objetivo principal del jugador a de ser conquistas el total del tablero y ser mas fuerte que el enemigo");miniPausa();
        System.out.println("___________DIPLOMACIA________"); //Diplomacia
        System.out.println("El juego dispone de un sistema diplomatico en el que podras administrar la relaciones bilaterales con el enemigo");miniPausa();
        System.out.println("Puedes optar por mantener una paz que te proporcione prosperidad ");miniPausa();
        System.out.println(" o por el contrario desenvainar la espada para saciar tus intereses ");miniPausa();
        System.out.println("___________Economia__________"); //Economia
        System.out.println("Uno de los objetivos del jugador debe ser gestionar su aldea y mantener una economia positiva");miniPausa();
        System.out.println("En Lord Of Strategy usamos como moneda de juego el ORO");miniPausa();
        System.out.println("la cual se desarrollara a medida que el jugador mejore edificios e infraestructuras");miniPausa();
        System.out.println("Estos edificios reportaran ingresos pero tambien gastos por mantenimiento");miniPausa();
        System.out.println("Existe diversidad de edificios como: Minas,granjas,fabricas,Misiles...");miniPausa();
        System.out.println();
        System.out.println("¡Advertencia!: Si el oro es negativo durante 4 turnos seguidos. Se generara ");miniPausa();
        System.out.println("un desgaste militar del 20% que ascendera cada 2 turnos en +15%");miniPausa();
        System.out.println("___________Niveles___________"); //Niveles
        System.out.println("En Lord Of Strategy ganaras XP por cada batalla u evento durante la partida.");miniPausa();
        System.out.println("Con la XP podras mejorar la tecnologia militar ofensiva o defensiva ");miniPausa();
        System.out.println("hay un total de 15 niveles de ataque y defensa");miniPausa();
        System.out.println("Cuanto mayor sea tu nivel mas fuerte y respetado seras");miniPausa();
        System.out.println("puedes mejorar tu tecnologia militar con el fin de mejorar tus habilidades en combate.");miniPausa();
        System.out.println("___________Batallas__________"); //batallas
        System.out.println("Las batallas son una forma de demostrar la fuerza de uno u otros contendientes. ");miniPausa();
        System.out.println("En Lord Of Strategy podras enfrentarte a tu rival siempre cuando estes en guerra con el");miniPausa();
        System.out.println("Durante las batallas se tendra en cuenta tu tecnologia militar, el nivel de los edificios militares y otros");miniPausa();
        System.out.println("___________________________________________________________________________________________________________");
        System.out.println("\u001B[0m");
    }
    public static void batalla(String player) {
        System.out.print("\033[34m");
        boolean isWIN=false;
        boolean endBattle=false;
        int DefPlayer=defensalvl(guerra[3]);
        if(memoria[3]>3){
            if(memoria[3]==4){
               DefPlayer-=DefPlayer*20/100;                         
            }else if(memoria[3]%2==0){
                DefPlayer-=(DefPlayer*15/100);
            }
        }
        int DefIA=defensalvl(guerra[1]);
        do{
            if(mejoras[8]>0){
                System.out.println("Tus cohetes han producido un daño de "+mejoras[8]*15);
            }
            if(mejorasIA[8]>0){
                System.out.println("Sus cohetes han producido un daño de "+mejorasIA[8]*15);
            }
            if(mejoras[9]>0 && mejorasIA[8]>0){
                int calculo=(mejorasIA[8]*15)-(mejoras[9]*10);
                System.out.println("Nuestros antimisiles han contrarrestado sus misiles en "+((mejorasIA[8]*15)-(mejoras[9]*10)));
                if(calculo>0){
                    DefPlayer-=calculo;
                }
            }else if (mejoras[8]>0){
                DefPlayer-=mejorasIA[8]*15;
            }
            if(mejorasIA[9]>0 && mejoras[8]>0){
                int calculo=(mejoras[8]*15)-(mejorasIA[9]*10);
                System.out.println("Sus antimisiles han contrarrestado nuestos misiles en "+((mejoras[8]*15)-(mejorasIA[9]*10)));
                if(calculo>0){
                    DefIA-=calculo;
                }
            }else if (mejorasIA[8]>0){
                DefIA-=mejoras[8]*15;
            }
            int AttackPlayer=ataquelvl(guerra[2]);
            if(((mejoras[2]*30)+(mejoras[3]*120)-(mejoras[7]*43))>=0 && mejoras[7]>0){
                AttackPlayer+=AttackPlayer*(mejoras[7]*8)/100;
                System.out.println("Tu ataque fue potenciado por tus fabricas en "+(mejoras[7]*8)+" %");
            }
            int AttackIA=ataquelvl(guerra[0]);
            if(((mejorasIA[2]*30)+(mejorasIA[3]*120)-(mejorasIA[7]*43))>=0 && mejoras[7]>0){
                AttackIA+=AttackIA*(mejorasIA[7]*8)/100;
            }
            int BonusPlayer=(int)(Math.random()*4);
                System.out.println("Jugador: ataque "+AttackPlayer +" defensa "+ DefPlayer); miniPausa();
                System.out.println("Enemigo: ataque "+AttackIA +" defensa "+ DefIA);miniPausa();miniPausa();
                if(BonusPlayer==1){
                    BonusPlayer=(int)(Math.random()º*(45-25)+25);
                    System.out.println("Tienes un bonus de "+BonusPlayer+"% es = +"+((AttackPlayer*BonusPlayer)/100)+" ataque");miniPausa();
                    DefIA=DefIA-(AttackPlayer+((AttackPlayer*BonusPlayer)/100));miniPausa();
                }else{
                    DefIA-=AttackPlayer;
                }
                int BonusIA=(int)(Math.random()*4);
                if(BonusIA==1){//bonus de la IA
                    BonusIA=1+(int)(Math.random()*(45-25)+25);
                    System.out.println("El enemigo tiene un bonus de "+BonusIA+"% es = +"+((AttackIA*BonusIA)/100)+" ataque");miniPausa();
                    DefPlayer=DefPlayer-(AttackIA+((AttackIA*BonusIA)/100));miniPausa();
                }else{
                    DefPlayer-=AttackIA;
                }
                //de esta manera evitaremos que se imprima valores negativos en la vida restante
                if(DefPlayer<0){
                    DefPlayer=0;
                }
                if(DefIA<0){
                    DefIA=0;
                } 
                DefIA=((int)(DefIA*100)/100);
                DefPlayer=((int)(DefPlayer*100)/100);
            System.out.println("vida restante de "+player+" es "+DefPlayer);miniPausa();
            System.out.println("vida restante enemigo "+DefIA);miniPausa();
            pausa();
            isWIN=false;
            if(DefPlayer<=0 || DefIA<=0){
                endBattle=true;
                if(DefPlayer>DefIA){
                    isWIN=true;
                    guerra[4]++;
                    valores[2]--;
                    if(guerra[2]>=guerra[3]){//le estaremos dando oro al jugador por batalla ganado. bonificado por el mayor de los niveles del jugador *0.5
                        memoria[4]+=(int)(Math.random()*(13+1)*(guerra[2]*0.5));
                    }else{
                        memoria[4]+=(int)(Math.random()*(13+1)*(guerra[3]*0.5));
                    }
                    System.out.println("has ganado");
                    valores[0]+=(int)(Math.random()*(200-50)+50);
                    valores[1]+=(int)(Math.random()*(90-40)+40);
                }else{
                    System.out.println("has perdido");
                    valores[1]+=(int)(Math.random()*(200-50)+50);
                    valores[0]+=(int)(Math.random()*(90-40)+40);
                    valores[2]++;
                    guerra[4]--;
                    if(guerra[0]>=guerra[1]){//le estaremos dando oro al jugador por batalla ganado. bonificado por el mayor de los niveles del jugador *0.5
                        valores[5]+=(int)(Math.random()*(13+1)*(guerra[0]*0.5));
                    }else{
                        valores[5]+=(int)(Math.random()*(13+1)*(guerra[1]*0.5));
                    }
                }
            }
        }while(!endBattle);
        System.out.print("\u001B[0m");
    }
    public static int[] lvlUpAttack(int xp,int lvl) {
        boolean es=false;
        boolean ia=false;
        int[]array={xp,lvl,0};//[0] almacenara xp restante [1] almacenara el nivel
        if(xp>=nivel(lvl+1,es,ia) && lvl<15){
            confirmar("Despues de esta accion tendremos "+(xp-nivel(lvl+1, es, ia))+" Tu nivel de ataque ascendera al nivel "+lvl+1);
            xp-=nivel(lvl+1, es,ia=true);
            lvl++;
            array[2]=1; //endTurno=true
        }else{
            if(lvl>=15){
                System.out.println("No puedes subir mas niveles");
            }else{
            System.out.println("No tienes suficiente xp");
            }
        }
        array[0]=xp;
        array[1]=lvl;
        return array;
    }
    public static int[] lvlUpDefensa(int xp,int lvl) {
        boolean es=true;
        boolean ia=false;
        int[]array={xp,lvl,0};//[0] almacenara xp restante [1] almacenara el nivel
        if(xp>=nivel(lvl+1,es,ia) && lvl<15){
            confirmar("Despues de esta accion tendremos "+(xp-nivel(lvl+1, es, ia))+" Tu nivel de defensa ascendera al nivel "+lvl+1);
            xp-=nivel(lvl+1, es,ia);
            lvl++;
            array[2]=1; //endTurno=true
        }else{
            if(lvl>=15){
                System.out.println("No puedes subir mas niveles");
            }else{
            System.out.println("No tienes suficiente xp");
            }
        }
        array[0]=xp;
        array[1]=lvl;
        return array;
    }
    public static void creditos(){
        System.out.println( "\033[35m");
        escribir("Muchas gracias por jugar Lord Of Strategy :)");miniPausa();System.out.println();
        escribir("______________________________________");System.out.println();
        escribir("Desarrollado: por Luis Hidalgo Aguilar");System.out.println();
        escribir("______________________________________");miniPausa();System.out.println();
        escribir("Dirgido por: Luis Hidalgo Aguilar");System.out.println();
        escribir("______________________________________");miniPausa();System.out.println();
        escribir("Diseñado por: Luis Hidalgo Aguilar");System.out.println();
        escribir("______________________________________");miniPausa();System.out.println();
        escribir("Testeado por: ");System.out.println();
        escribir("Jose Javier Galan Salazar");System.out.println();
        escribir("Luis Hidalgo Aguilar");System.out.println();
        escribir("______________________________________");miniPausa();System.out.println();
        escribir("Agradecimientos: ");System.out.println();
        escribir("Jose Javier Galan Salazar");System.out.println();
        escribir("Daniel Garcia Zamora");System.out.println();
        escribir("______________________________________");miniPausa();System.out.println();
        escribir("\u001B[0m");System.out.println();
    }
    public static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.println("\u001B[0m");
    }
    // -------------------------------SUBMENUS----------------------------------
                //MEJORAS
                public static boolean mejoras(){
                    boolean endTurno=false;
                    System.out.println("\033[31m");
                    System.out.println("_______________________Menu mejoras________________");miniPausa();
                    System.out.println("1. Granjas : Cuestan 200 oro, genera +30 oro ademas genera +20 alimentos para fabricas alimentarias.");
                    System.out.println("Tienen un mantenimiento de -5 oro");
                    System.out.println();miniPausa();
                    System.out.println("2. Pesca: Cuesta 300 oro, genera +40 oro y genera +35 alimentos para fabricas alimentarias.");
                    System.out.println("Tiene un mantenimiento de -7 oro");
                    System.out.println();miniPausa();
                    System.out.println("3. Minas: Cuestan 500 oro, genera +80 oro ademas generan +30 recursos para");
                    System.out.println("Fabricas bienes y militares respectivamente. Tiene un mantenimiento de -23 oro");
                    System.out.println();miniPausa();
                    System.out.println("4. Petroleras: cuesta 800 oro generan +100 oro ademas genera +120 recursos ");
                    System.out.println("para fabricas militares. Tienen un mantenimiento de -40 oro");
                    System.out.println();miniPausa();
                    System.out.println("5. Aserrerias: Cuesta 400 oro, genera +30 oro y generan +50 recursos para fabricas bienes.");
                    System.out.println("Tienen un mantenimiento de -12 oro");
                    System.out.println();miniPausa();
                    System.out.println("6. FabricaAlimentaria: Cuesta 600 oro, genera +60 oro. Consumen -43 alimentos");
                    System.out.println("y tienen un mantenimiento de -20 oro");
                    System.out.println();miniPausa();
                    System.out.println("7. FabricaBienes: Cuesta 600 oro, genera +100 oro. Consumen -43 recursos");
                    System.out.println("y tienen un mantenimiento de -25 oro");
                    System.out.println();miniPausa();
                    System.out.println("8. FabricaMilitar: Cuesta 400 oro, generan un bonus de +8% ataque. Consumen -70 recursos");
                    System.out.println("y tienen un mantenimiento de -50 oro");
                    System.out.println();miniPausa();
                    System.out.println("9. LanzaMisiles: Cuesta 150 oro, generan un daño de +15 ataque y tienen un mantenimiento de -50 oro");
                    System.out.println();miniPausa();
                    System.out.println("10. AntiMisiles: Cuesta 100 oro, restan el daño de misiles enemigos en - 10 y tienen un mantenimiento de -40 oro");
                    System.out.println();miniPausa();
                    System.out.println("11. balance: muestra todos tus beneficios y gastos generados por tus edificios");miniPausa();
                    System.out.println("12. Edificios: Muestra el nivel de todos los edificios disponibles");
                    System.out.println("13. Puedes destruir un nivel edificio escribiendo ejemplo: (destruirGranjas)");miniPausa();
                    System.out.println("14. atras: vuelve al menu principal");miniPausa();
                    System.out.println("________________________________________________________");
                    System.out.println("\u001B[0m");
                    for(int i=0;i==0 && endTurno==false;){
                        Boolean confirm=false;
                            String comando=leeComando("(Mejoras) introduce comando");
                            if(comando.equals("GRANJAS")&& valores[4]>=200){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-200)+" de oro. Las granjas subiran de nivel al "+ (mejoras[0]+1));
                                if(confirm==true){
                                    mejoras[0]++;valores[4]-=200;
                                    System.out.println("Tus granjas son de nivel "+ mejoras[0]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("PESCA")&& valores[4]>=300){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-300)+" de oro. Los edificios de Pesca subiran de nivel al "+ (mejoras[1]+1));
                                if(confirm==true){
                                    mejoras[1]++;valores[4]-=300;
                                    System.out.println("Tus granjas son de nivel "+ mejoras[1]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("MINAS")&& valores[4]>=500){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-500)+" de oro. Las Minas subiran de nivel al "+ (mejoras[2]+1));
                                if(confirm==true){
                                    mejoras[2]++;valores[4]-=500;
                                    System.out.println("Tus Minas son de nivel "+ mejoras[2]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("PETROLERAS")&& valores[4]>=800){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-800)+" de oro. Las Petroleras subiran de nivel al "+ (mejoras[3]+1));
                                if(confirm==true){
                                    mejoras[3]++;valores[4]-=800;
                                    System.out.println("Tus Petroleras son de nivel "+ mejoras[3]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("ASERRERIAS")&& valores[4]>=400){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-400)+" de oro. Las Aserrerias subiran de nivel al "+ (mejoras[4]+1));
                                if(confirm==true){
                                    mejoras[4]++;valores[4]-=400;
                                    System.out.println("Tus Aserrerias son de nivel "+ mejoras[4]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("FABRICAALIMENTARIA")&& valores[4]>=600){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-600)+" de oro. Las Fabricas alimentarias subiran de nivel al "+ (mejoras[5]+1));
                                if(confirm==true){
                                    mejoras[5]++;valores[4]-=600;
                                    System.out.println("Tus Fabricas alimentarias son de nivel "+ mejoras[5]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("FABRICABIENES")&& valores[4]>=600){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-600)+" de oro. Las Fabricas de bienes subiran de nivel al "+ (mejoras[6]+1));
                                if(confirm==true){
                                    mejoras[6]++;valores[4]-=600;
                                    System.out.println("Tus Fabricas de bienes son de nivel "+ mejoras[6]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("FABRICAMILITAR")&& valores[4]>=400){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-400)+" de oro. Las Fabricas Militares subiran de nivel al "+ (mejoras[7]+1));
                                if(confirm==true){
                                    mejoras[7]++;valores[4]-=400;
                                    System.out.println("Tus Fabricas Militares son de nivel "+ mejoras[7]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("LANZAMISILES")&& valores[4]>=150){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-150)+" de oro. Los Lanza Misiles subiran de nivel al "+ (mejoras[8]+1));
                                if(confirm==true){
                                    mejoras[8]++;valores[4]-=150;
                                    System.out.println("Tus Lanza Misiles son de nivel "+ mejoras[8]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("ANTIMISILES")&& valores[4]>=100){
                                confirm=confirmar("Despues de esta accion tendremos "+(valores[4]-100)+" de oro. Los Anti Misiles subiran de nivel al "+ (mejoras[9]+1));
                                if(confirm==true){
                                    mejoras[9]++;valores[4]-=100;
                                    System.out.println("Tus Anti Misiles son de nivel "+ mejoras[9]);
                                    endTurno=true;
                                }else{
                                    System.out.println("Decidistes no mejorar");
                                }
                                System.out.println("________________________________________________");
                            }else if(comando.equals("BALANCE")){
                                if(memoria[0]>=0){
                                    System.out.println("Tu beneficio fue +"+memoria[0]+" oro.");
                                    System.out.println("Tus ingresos fueron de +"+memoria[1]+" de oro");
                                    System.out.println("Tus gastos en mantenimiento es de -"+memoria[2]+" de oro");
                                    System.out.println("Tu balance de recrusos alimentarios son "+((mejoras[0]*20)+(mejoras[1]*35)-(mejoras[5]*43)));
                                    System.out.println("Tu balance de recursos de bienes son "+((mejoras[2]*30)+(mejoras[4]*50)-(mejoras[6]*43)));
                                    System.out.println("Tu balance de recursos militares son "+((mejoras[2]*30)+(mejoras[3]*120)-(mejoras[7]*70)));
                                }else{
                                    System.out.println("Tus gastos ascendieron a "+memoria[0]+" oro");
                                    System.out.println("Tus ingresos fueron de +"+memoria[1]+" de oro");
                                    System.out.println("Tus gastos en mantenimiento es de -"+memoria[2]+" de oro");
                                    System.out.println("Tu balance de recrusos alimentarios son "+((mejoras[0]*20)+(mejoras[1]*35)-(mejoras[5]*43)));
                                    System.out.println("Tu balance de recursos de bienes son "+((mejoras[2]*30)+(mejoras[4]*50)-(mejoras[6]*43)));
                                    System.out.println("Tu balance de recursos militares son "+((mejoras[2]*30)+(mejoras[3]*120)-(mejoras[7]*70)));
                                }
                            }else if(comando.equals("EDIFICIOS")){
                                System.out.println("Granjas lvl "+mejoras[0]);
                                System.out.println("Muelles pesca lvl "+mejoras[1]);
                                System.out.println("Minas lvl "+mejoras[2]);
                                System.out.println("Petroleras lvl "+mejoras[3]);
                                System.out.println("Aserrerias lvl "+mejoras[4]);
                                System.out.println("Fabricas alimentarias lvl "+mejoras[5]);
                                System.out.println("Fabricas bienes lvl "+mejoras[6]);
                                System.out.println("Fabricas militares lvl "+mejoras[7]);
                                System.out.println("Lanza misiles lvl "+mejoras[8]);
                                System.out.println("Anti misiles lvl "+mejoras[9]);
                            }else if(comando.charAt(0)=='D'){ // analizamos si el usuario a introducido el comando Destruir para luego dentro de la funcion (destruir) analizar el resto del comando 
                                String buscar="DESTRUIR";
                                boolean continuar=true;
                                if (comando.length()>6){
                                    for(int e=0;e<7 && continuar==true;e++){
                                    if(comando.charAt(e)!=buscar.charAt(e)){
                                            continuar=false;
                                    }
                                    if(e==6){
                                           endTurno=destruir(comando);
                                            
                                    }
                                    }
                                }
                            }else if(comando.equals("ATRAS")){
                                i++;
                            }
                    }
                    return endTurno;
                }
                public static boolean destruir(String comando){
                    boolean endTurno=false;
                    boolean confirm=false;
                    if(comando.equals("DESTRUIRGRANJA") && mejoras[0]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[0]-1)+" Granjas");
                        if(confirm==true){
                            mejoras[0]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRPESCA")&& mejoras[1]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[1]-1)+" Puertos pesqueros");
                        if(confirm==true){
                            mejoras[1]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRMINAS")&& mejoras[2]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[2]-1)+" Minas");
                        if(confirm==true){
                            mejoras[2]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRPETROLERAS")&& mejoras[3]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[3]-1)+" Petroleras");
                        if(confirm==true){
                            mejoras[3]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRASERRERIAS")&& mejoras[4]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[4]-1)+" Aserrerias");
                        if(confirm==true){
                            mejoras[4]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRFABRICAALIMENTARIA")&& mejoras[5]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[5]-1)+" Fabricas Alimentarias");
                        if(confirm==true){
                            mejoras[5]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRFABRICABIENES")&& mejoras[6]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[6]-1)+" Fabricas de Bienes");
                        if(confirm==true){
                            mejoras[6]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRFABRICAMILITAR")&& mejoras[7]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[7]-1)+" Fabricas militares");
                        if(confirm==true){
                            mejoras[7]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRLANZAMISILES")&& mejoras[8]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[8]-1)+" Lanza misiles");
                        if(confirm==true){
                            mejoras[8]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }else if(comando.equals("DESTRUIRANTIMISILES")&& mejoras[9]>0){
                        confirm=confirmar("Despues de esta accion tendremos "+ (mejoras[9]-1)+" Anti misiles");
                        if(confirm==true){
                            mejoras[9]--;
                            endTurno=true;
                        }else{
                            System.out.println("Decidistes no mejorar");
                        }
                    }
                    return endTurno;
                }
                //MILITAR
                public static void militar(){
                    System.out.println("_______________________Menu militar________________");miniPausa();
                    System.out.println("1. ataque : Muestra el rango ataque del jugador");miniPausa();
                    System.out.println("2. defensa: Muestra el rango defensa del jugador");miniPausa();
                    System.out.println("3. ataqueIA: Muestra el rango ataque del enemigo");miniPausa();
                    System.out.println("4. defensaIA: Muestra el rango defensa del Enemigo");miniPausa();
                    System.out.println("5. tablero: Muestra el total de zonas del tablero que posees");miniPausa();
                    System.out.println("6. atras: vuelve al menu principal");miniPausa();
                    System.out.println("________________________________________________________");
                    for(int i=0;i==0;){
                        String comando=leeComando("(Militar) introduce comando");
                        if(comando.equals("ATAQUE")){
                            ataque(guerra[2]);miniPausa();
                            System.out.println("________________________________________________");
                        }else if(comando.equals("DEFENSA")){
                            defensa(guerra[3]);miniPausa();
                            System.out.println("________________________________________________");
                        }else if(comando.equals("ATAQUEIA")){
                            ataque(guerra[0]);miniPausa();
                            System.out.println("________________________________________________");
                        }else if(comando.equals("DEFENSAIA")){
                            defensa(guerra[1]);miniPausa();
                            System.out.println("________________________________________________");
                        }else if(comando.equals("TABLERO")){
                            tablero();miniPausa();
                            System.out.println("________________________________________________");
                        }else if(comando.equals("ATRAS")){
                            i++;
                        }
                    }
                }
                    public static void ataque(int lvl){
                        System.out.println("\033[33m");
                        if(lvl==1){
                            System.out.println("Rango de ataque es de 1-11");
                        }
                        if(lvl==2){
                            System.out.println("Rango de ataque es de 7-14");
                        }
                        if(lvl==3){
                            System.out.println("Rango de ataque es de 10-24");
                        }
                        if(lvl==4){
                            System.out.println("Rango de ataque es de 20-31");
                        }
                        if(lvl==5){
                            System.out.println("Rango de ataque es de 27-35");
                        }
                        if(lvl==6){
                            System.out.println("Rango de ataque es de 30-38");
                        }
                        if(lvl==7){
                            System.out.println("Rango de ataque es de 34-47");
                        }
                        if(lvl==8){
                            System.out.println("Rango de ataque es de 43-56");
                        }
                        if(lvl==9){
                            System.out.println("Rango de ataque es de 67-80");
                        }
                        if(lvl==10){
                            System.out.println("Rango de ataque es de 77-100");
                        }
                        if(lvl==11){
                            System.out.println("Rango de ataque es de 80-120");
                        }
                        if(lvl==12){
                            System.out.println("Rango de ataque es de 100-128");
                        }
                        if(lvl==13){
                            System.out.println("Rango de ataque es de 110-140");
                        }
                        if(lvl==14){
                            System.out.println("Rango de ataque es de 130-148");
                        }
                        if(lvl==15){
                            System.out.println("Rango de ataque es de 140-170");
                        }
                        System.out.println( "\u001B[0m");
                    }
                    public static void defensa(int lvl){
                        System.out.println("\033[33m");
                        if(lvl==1){
                            System.out.println("Rango de defensa es de 20-41");
                        }
                        if(lvl==2){
                            System.out.println("Rango de defensa es de 36-81");
                        }
                        if(lvl==3){
                            System.out.println("Rango de defensa es de 74-101");
                        }
                        if(lvl==4){
                            System.out.println("Rango de defensa es de 81-116");
                        }
                        if(lvl==5){
                            System.out.println("Rango de defensa es de 95-151");
                        }
                        if(lvl==6){
                            System.out.println("Rango de defensa es de 120-160");
                        }
                        if(lvl==7){
                            System.out.println("Rango de defensa es de 136-181");
                        }
                        if(lvl==8){
                            System.out.println("Rango de defensa es de 174-201");
                        }
                        if(lvl==9){
                            System.out.println("Rango de defensa es de 181-216");
                        }
                        if(lvl==10){
                            System.out.println("Rango de defensa es de 195-241");
                        }
                        if(lvl==11){
                            System.out.println("Rango de defensa es de 220-261");
                        }
                        if(lvl==12){
                            System.out.println("Rango de defensa es de 236-281");
                        }
                        if(lvl==13){
                            System.out.println("Rango de defensa es de 274-301");
                        }
                        if(lvl==14){
                            System.out.println("Rango de defensa es de 281-316");
                        }
                        if(lvl==15){
                            System.out.println("Rango de defensa es de 295-351");
                        }
                        System.out.println( "\u001B[0m");
                    }
    //--------------------------------------------------PAUSAS-------------------------------
    public static void pausa(){
        try{ Thread.sleep(300); 
            System.out.print(".");
            Thread.sleep(300); 
            System.out.print(".");
            Thread.sleep(300); 
            System.out.print(".");
            Thread.sleep(1000); 
            System.out.println();
        }catch(InterruptedException e ){}
    }
    public static void miniPausa(){
        try{ Thread.sleep(200); 
        }catch(InterruptedException e ){}
    }
    public static void pausaEscribir(){
        try{ Thread.sleep(20); 
        }catch(InterruptedException e ){}
    }
    public static void escribir(String msn){
        for(int i=0;i<msn.length();i++ ){
            System.out.print(msn.charAt(i));pausaEscribir();
        }
    }
}
//--------------------------------------------------------------------------------------
/* DIARIO DESARROLLO:
 * almacenar en una base de datos todas las variables (guerra,valores)
 * Implementar siempre que sea necesario forEach
 * implementar un comando capaz de poder negociar con la IA y esta este capacitada para negociar
*/

/*BUGS:
 * 
 */

 /*
  String blanco = "\u001B[0m";  
  String negro = "\033[30m";s
  String rojo = "\033[31m";
  String verde = "\033[32m";
  String amarillo = "\033[33m";
  String azul = "\033[34m";
  String magenta = "\033[35m";
  String celeste = "\033[36m";
  String blanco = "\033[37m";
  */