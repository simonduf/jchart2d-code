#!/bin/sh
#
#       Sample shell script to fetch your project HTML
# As soon as a proof of working cronjob is at hand, pipe to > /dev/null and set wget to quiet -q
/usr/bin/wget -v -O ~/projnews.tmp 'http://sourceforge.net/export/projnews.php?group_id=50440&limit=10&flat=1&show_summaries=1'
/bin/mv -f ~/projnews.tmp /home/groups/j/jc/jchart2d/htdocs/projnews.cache