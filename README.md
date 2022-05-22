# M3U-VodDownloader-JE

Video on Demand or VOD, are huge database of videos/film/media stored into a file ( .m3u or .m3u8 ), you can open it with VLC and see all the contents.

I made this script to manage better this "huge" database of media and download my preferred! with this script you can: -> Manage better channels and categories into the .m3u file -> Make your own download list of media -> Download file with proxies (You don't need to import that, the script will download proxies for you)

M3U-VodDownloader Java Edition. (rewitten from my old project in python)
- Youtube video tutorial: https://youtu.be/NGGlMFesvko
- Written in Java (version 17)
- External library: jSoup 1.14
- Now available with proxy 

Note: If you have some probem with downloads, try to turn off your antivirus (for example, Kaspersky can block the download)

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
