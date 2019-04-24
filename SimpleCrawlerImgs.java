

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
	private static Queue<String> auxQ=new LinkedList<String>();//辅助广度优先搜索的队列
	private static HashSet<String> visited=new HashSet<>();//防止重复查询
	
	public static void inputURL(String iURL)
	{
		URL=iURL;
	}
	
	public static void link() throws IOException
	{
		doc=Jsoup.connect(URL).get();
		visited.add(URL);
	}
	
	public static void getAllImgs()//遍历所有图片
	{
		Elements media=doc.select("[src]");
		System.out.println("图片");
		for(Element src:media)
		{
			String url=src.attr("abs:src");//获取文件路径
			if(visited.contains(url)||url.equals(""))continue;//去除无效项
			if(src.tagName().equals("img"))
			{
				System.out.println(url);//获取文件绝对路径		
				try
				{
					downLoadImg(url);//下载至指定文件夹
				}
				catch(IOException e)
				{
					
				}
			}
		}
	}
	
	public static void downLoadImg(String url) throws IOException	//下载一张图片
	{
		URL u;
		URLConnection uc;
		try
		{
			//连接不成功时直接舍弃
			u = new URL(url);
			uc=u.openConnection();
		}
		catch(IOException e)
		{
			return;
		}
		//获取数据流
        InputStream is=uc.getInputStream();
        //获取后缀名
        String imageName = url.substring(url.lastIndexOf("/") + 1,url.length());
        //写入本地
        FileOutputStream os = new FileOutputStream(new File("E:\\jsoup\\img", imageName));
        byte[] b = new byte[1024];
        int i=0;
        while((i=is.read(b))!=-1){
          os.write(b, 0, i);
        }
        is.close();
        os.close();
	}
	
	public static void getAllLinks()//获取页面中的链接
	{
		Elements links=doc.select("a[href]");
		System.out.println("链接:");
		for(Element link:links)
		{
			String linkURL=link.attr("abs:href");//获取链接路径
			System.out.println(linkURL);
			if(visited.contains(linkURL)||linkURL.equals(""))continue;//除去无效链接
			if(auxQ.size()<200)auxQ.add(linkURL);
		}
	}
	
	public static void handleAPage(String URL)//对于一张页面进行处理
	{
		inputURL(URL);
		try
		{
			link();
		}
		catch(IOException e)
		{
			//连接不成功时直接退出
			return;
		}
		getAllImgs();
		System.out.println();
		getAllLinks();
	}

	public static void main(String[] args){
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		System.out.println("请输入起始地址：");
		String url=input.next();
		handleAPage(url);
		for(int i=0;i<20;i++)
		{
			handleAPage(auxQ.poll());
		}
	}

}
