# HomeMirror
Android application powering the mirror in my house

<img src="https://raw.githubusercontent.com/HannahMitt/HomeMirror/master/design/IMG_20151121_183522%20(1).jpg" width="400"/>

### Note: code unmaintained
This was a fun project, and I may pick it back up again one day.
You can still follow the instructions for putting a mirror together, but may need to bring your own light-text-on-dark-background app.

Software
====
* Day, time, and weather display
* Birthday messages
* Chore reminders
* Biking weather recommendation
* Stock price swings
* Today's new XKCD
* Next calendar event
* Top news headline
* Face detection with mood detection

APIs provided by Yahoo Finance, Forecast.io, the BBC, and XKCD

<img src="https://raw.githubusercontent.com/HannahMitt/HomeMirror/master/design/HomeMirror.png" width="400"/>

How to Run
----
1. Clone project
2. Get a [forecast.io api key](https://developer.forecast.io/)
3. Add a keys.xml file under res/values with \<string name="dark_sky_api_key">your_key\</string>
4. Import project to Android Studio and run

When its mirror time, I also recommend turning on the device Developer Option: "Stay Awake - Screen will never sleep while charging"

Making a Mirror
====

[Video walk-through on adafruit (Thank you Becky!)](https://learn.adafruit.com/android-smart-home-mirror)

1. Obtain Android device
----
We had too many. Sad old devices are ideal.
I used a [2012 Nexus 7](http://www.amazon.com/gp/offer-listing/B009X3UW2G/ref=olp_tab_refurbished?ie=UTF8&condition=refurbished&qid=1441327955&sr=8-1).

2. Obtain two-way mirror
---
We did this the old-fashioned way, and quested to [Canal Plastic Center](http://canalplastic.com/). A beautiful and highly recommended establishment. 

[Wiki of places to buy two-way mirrors](https://github.com/HannahMitt/HomeMirror/wiki/Places-to-buy-a-two-way-mirror)

Get a mirror at least as large as your Android device. My mirror is 6"x12" with 1/8 thickness. 

3. Sticky stuff for mounting
---
Our trickiest problem was figuring out how to mount everything with minimal damage and error.
We eventually discovered [reusable double-sided adhesive](http://www.amazon.com/Command-Assorted-8-Small-4-Medium-4-Large/dp/B0084M68IO/ref=pd_sim_sbs_229_6?ie=UTF8&refRID=1TX12CR5RF0RTP6CKJR7&dpSrc=sims&dpST=_AC_UL320_SR202%2C320_), which is amazing. 

We're using it both to mount the mirror to the device, and the device to the wall.

4. Attach black backing
---
We used black construction paper. If your device and mirror are awesomely identical in size, you can skip this.

Note: You want to glue it well, so the backing doesn't tear when the adhesive is holding it.

* Cut a piece of black backing the same size as your mirror
* Decide where you want your device to show through. We did upper right. Leave a border all the way around for adhesive.
* At that location, carefully cut a hole in the backing the same size as your device
* CAREFULLY glue the backing to the mirror. We used spray adhesive, and practiced how we would pick it up and lay it on the mirror a couple times before going for it. We also wore black surgical gloves.
 
<img src="https://raw.githubusercontent.com/HannahMitt/HomeMirror/master/design/IMG_20150911_110449-2.jpg" height="400"/>

5. Adhesion
---
Fire up the app on the device.

Put the adhesive along the bezel or edges of the device. Line up the mirror and stick it on there.

6. Wall spot
---
You probably want a long usb cable. Find a wall spot where you can keep the device plugged in.

Put a bunch of adhesive on the back of the device, and stick it on there.

<img src="https://raw.githubusercontent.com/HannahMitt/HomeMirror/faed8d927b93ec2c38159d8e3968f8133511ee67/design/thumbs_up_mirror.jpg" width="400"/>

Links
===
At the heart of this project, is 'put a mirror on it'. [Check out alternative mirror projects and feel free to add your own reflections](https://github.com/HannahMitt/HomeMirror/wiki/Other-mirror-projects-with-alternate-technologies)
