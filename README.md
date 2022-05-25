# M3U-VodDownloader-JE

Video on Demand or VOD, are huge database of videos/film/media stored into a file ( .m3u or .m3u8 ), you can open it with VLC and see all the contents.

![image](https://user-images.githubusercontent.com/44652155/170267752-2104e7e5-6fec-4318-9000-b6b51a9fe893.png)


I made this script to manage better this "huge" database of media and download my preferred! with this script you can: 

- Manage better channels and categories into the .m3u file 
- Create your own download list of media!
- Download media with proxies (You don't need to import that, the program will download proxies for you)


![image](https://user-images.githubusercontent.com/44652155/170266183-c310d8c1-6ee8-4a51-bdf6-5b3a2eac1d69.png)

M3U-VodDownloader Java Edition. (rewitten from my old project in python)
- Youtube video tutorial: https://www.youtube.com/watch?v=wwnwqEWBSR4
- Written in Java (version 17)
- External library: jSoup 1.14

![image](https://user-images.githubusercontent.com/44652155/170266291-92d9e857-d3f9-4e77-b39c-b3e0e2d0d824.png)

- Now available with proxy 

![image](https://user-images.githubusercontent.com/44652155/170266405-1b750eb9-f897-4ff9-9cb1-476a93e4f2c5.png)



Note: If you have some probem with downloads, try to turn off your antivirus (for example, Kaspersky can block the download)

![image](https://user-images.githubusercontent.com/44652155/170266839-079e8662-3f2a-4a5c-aa0d-bbf69acaacfc.png)

Download Java 17 (x64 windows) : https://bit.ly/3MFFDEj

Instructions:

1) Open CMD, and check the java version (need 17). 
- $ java -version
2) Go to directory out/production/M3U-VODownloader
- $ cd {project_path}/out/production/M3U-VODownloader
3) Run the program for the first time:
- $ java com.github.alfix00.M3UVodDownloader

When you run it for the first time, the script generate 3 folder (m3u_folder, download_list, proxy_folder).
Now, go to generated directory M3U-VodDownloader/m3u_folder and place your personal m3u vod file! 

4) Run again 
- $ java com.github.alfix00.M3UVodDownloader

- Enojy




If you wanna edit somethings, i recommend to use intelliJ CE
