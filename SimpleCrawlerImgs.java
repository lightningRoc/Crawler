

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleCrawlerImgs {
	
	private static Document doc;
	private static String URL;
	private static Queue<String> auxQ=new LinkedList<String>();//����������������Ķ���
	private static HashSet<String> visited=new HashSet<>();//��ֹ�ظ���ѯ
	
	public static void inputURL(String iURL)
	{
		URL=iURL;
	}
	
	public static void link() throws IOException
	{
		doc=Jsoup.connect(URL).get();
		visited.add(URL);
	}
	
	public static void getAllImgs()//��������ͼƬ
	{
		Elements media=doc.select("[src]");
		System.out.println("ͼƬ");
		for(Element src:media)
		{
			String url=src.attr("abs:src");//��ȡ�ļ�·��
			if(visited.contains(url)||url.equals(""))continue;//ȥ����Ч��
			if(src.tagName().equals("img"))
			{
				System.out.println(url);//��ȡ�ļ�����·��		
				try
				{
					downLoadImg(url);//������ָ���ļ���
				}
				catch(IOException e)
				{
					
				}
			}
		}
	}
	
	public static void downLoadImg(String url) throws IOException	//����һ��ͼƬ
	{
		URL u;
		URLConnection uc;
		try
		{
			//���Ӳ��ɹ�ʱֱ������
			u = new URL(url);
			uc=u.openConnection();
		}
		catch(IOException e)
		{
			return;
		}
		//��ȡ������
        InputStream is=uc.getInputStream();
        //��ȡ��׺��
        String imageName = url.substring(url.lastIndexOf("/") + 1,url.length());
        //д�뱾��
        FileOutputStream os = new FileOutputStream(new File("E:\\jsoup\\img", imageName));
        byte[] b = new byte[1024];
        int i=0;
        while((i=is.read(b))!=-1){
          os.write(b, 0, i);
        }
        is.close();
        os.close();
	}
	
	public static void getAllLinks()//��ȡҳ���е�����
	{
		Elements links=doc.select("a[href]");
		System.out.println("����:");
		for(Element link:links)
		{
			String linkURL=link.attr("abs:href");//��ȡ����·��
			System.out.println(linkURL);
			if(visited.contains(linkURL)||linkURL.equals(""))continue;//��ȥ��Ч����
			if(auxQ.size()<200)auxQ.add(linkURL);
		}
	}
	
	public static void handleAPage(String URL)//����һ��ҳ����д���
	{
		inputURL(URL);
		try
		{
			link();
		}
		catch(IOException e)
		{
			//���Ӳ��ɹ�ʱֱ���˳�
			return;
		}
		getAllImgs();
		System.out.println();
		getAllLinks();
	}

	public static void main(String[] args){
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		System.out.println("��������ʼ��ַ��");
		String url=input.next();
		handleAPage(url);
		for(int i=0;i<20;i++)
		{
			handleAPage(auxQ.poll());
		}
	}

}
