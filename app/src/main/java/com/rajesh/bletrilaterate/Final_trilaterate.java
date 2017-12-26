package com.rajesh.bletrilaterate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class Final_trilaterate extends AppCompatActivity {
HashMap<String,Integer> obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_trilaterate);
        obj = (HashMap<String,Integer>) getIntent().getExtras().get("hashmap");
        Log.v("hashmap_final",obj.toString());
    }

    class constraint implements Serializable
    {
        byte[] convert_str()
        {
            String str=Double.toString(xi)+"_"+Double.toString(yi)+"_"+Double.toString(rssi);
            return str.getBytes();
        }

        constraint(byte[] b)
        {
            //String s = b.toString();

            String s = new String(b);
            String[] d = s.split("_");
            Log.d("fdsa",s);
            xi=Double.parseDouble(d[0]);
            yi=Double.parseDouble(d[1]);
            rssi=Double.parseDouble(d[2]);
            System.out.println(xi+" "+yi+" "+rssi);


        }
        double xi,yi,r,rssi,p=-14,n=2.67;
        constraint()
        {
            xi=0;
            yi=0;
        }

        void s (double x,double y,double distance)
        {
            xi=x;
            yi=y;
            r=distance;
            rssi=0;
        }
        void set(double x,double y,double rssi)
        {
            xi=x;
            yi=y;
            this.rssi=rssi;
            r=Math.pow(10,((p+rssi) /26.70));
            // System.out.println("r = " +r);
        }
    }
    static double distance(double x1,double y1,double x2,double y2)
    {

        double q=Math.abs(x1-x2);
        double w=Math.abs(y1-y2);
        double dist=Math.sqrt((q*q)+(w*w));
        return dist;
    }

    static double nCr(int n, int r){
        int rfact=1, nfact=1, nrfact=1,temp1 = n-r ,temp2 = r;
        if(r>n-r)
        {
            temp1 =r;
            temp2 =n-r;
        }
        for(int i=1;i<=n;i++)
        {
            if(i<=temp2)
            {
                rfact *= i;
                nrfact *= i;
            }
            else if(i<=temp1)
            {
                nrfact *= i;
            }
            nfact *= i;
        }
        return nfact/(double)(rfact*nrfact);
    }




    static void getcommon(constraint c1,constraint c2,int p,double arr[][])
    {
        double x1,y1,x2,y2,xt,yt;
        double d= distance(c1.xi,c1.yi,c2.xi,c2.yi);


        if(d>c1.r+c2.r)
        {
            //System.out.println("inside if");
            double k1=c1.r;
            double k2=d-c1.r;

            x1=((k1*c2.xi)+(k2*c1.xi))/(k1+k2);
            y1=((k1*c2.yi)+(k2*c1.yi))/(k1+k2);

            double k11=c2.r;
            double k22=d-c2.r;

            x2=((k11*c1.xi)+(k22*c2.xi))/(k11+k22);
            y2=((k11*c1.yi)+(k22*c2.yi))/(k11+k22);

            //System.out.println(" Inner point = " + x1 + " " + y1);
            //	System.out.println(" outer point = " + x2 + " " + y2);

            xt=(x1+x2)/2;
            yt=(y1+y2)/2;

            //System.out.println(" avg point = " + xt + " " + yt);

        }
        else if (d+c2.r < c1.r)
        {
            System.out.println("inside first elseif");
            double k1=c2.r;
            double k2=c2.r+d;
            x1=((k1*c1.xi)-(k2*c2.xi))/(k1-k2);
            y1=((k1*c1.yi)-(k2*c2.yi))/(k1-k2);

            double k11=c2.r+(c1.r-k2);
            double k22=c1.r;

            x2=((k11*c1.xi)-(k22*c2.xi))/(k11-k22);
            y2=((k11*c1.yi)-(k22*c2.yi))/(k11-k22);

            //System.out.println(" Inner point = " + x1 + " " + y1);
            //System.out.println(" outer point = " + x2 + " " + y2);

            xt=(x1+x2)/2;
            yt=(y1+y2)/2;

            //System.out.println(" point = " + xt + " " + yt);
        }
        else if (d+c1.r < c2.r)
        {
            System.out.println("inside 2nd elsif");
            double k1=c1.r;
            double k2=c1.r+d;
            x1=((k1*c2.xi)-(k2*c1.xi))/(k1-k2);
            y1=((k1*c2.yi)-(k2*c1.yi))/(k1-k2);

            double k11=c1.r+(c2.r-k2);
            double k22=c2.r;

            x2=((k11*c2.xi)-(k22*c1.xi))/(k11-k22);
            y2=((k11*c2.yi)-(k22*c1.yi))/(k11-k22);

            //System.out.println(" Inner point = " + x1 + " " + y1);
            //System.out.println(" outer point = " + x2 + " " + y2);

            xt=(x1+x2)/2;
            yt=(y1+y2)/2;

            //System.out.println(" point = " + xt + " " + yt);
        }


        else
        {
            double a = ((c1.r*c1.r)-(c2.r*c2.r)+(d*d))/(2*d);

            double h=Math.sqrt((c1.r*c1.r)-(a*a));

            xt = c1.xi + (a * (c2.xi-c1.xi)) / d;

            yt = c1.yi + (a * (c2.yi-c1.yi)) / d;

            x1=xt+(h*(c2.yi-c1.yi))/d;
            y1=yt-(h*(c2.xi-c1.xi))/d;
            x2=xt-(h*(c2.yi-c1.yi))/d;
            y2=yt+(h*(c2.xi-c1.xi))/d;
            //System.out.println(" first point " + x1 + " " + y1);
            // System.out.println(" second point " + x2 + " " + y2);
        }
        arr[p][0]=x1;
        arr[p][1]=y1;
        arr[p][2]=x2;
        arr[p][3]=y2;
    }

}
