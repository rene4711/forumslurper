System.properties.with { p ->
	p['geb.browser']='htmlunit'
	p['org.apache.commons.logging.Log']='org.apache.commons.logging.impl.NoOpLog'
	//p['org.apache.commons.logging.Log']='org.apache.commons.logging.impl.SimpleLog'
	//p['org.apache.commons.logging.simplelog.log.com.gargoylesoftware.htmlunit']='NONE'
}

@Grapes([
	@Grab("org.gebish:geb-core:latest.release"),
	@Grab("org.seleniumhq.selenium:selenium-htmlunit-driver:2.26.0")
])

import geb.Browser
 
Browser.drive {
	
	def subforumLandingPageLink = "http://forum.viva.nl/forum/Gezondheid/list_topics/6"
	go subforumLandingPageLink
	assert title == "Viva - Onderwerpen van forum Gezondheid"
	println "Processing ${title}"
 
	def lastPageLink = $("dl.discussion-navigation.page-navigation.before dd a", rel: "next").previous()
	assert lastPageLink.text() == "526"
	def lastPageNumber = lastPageLink.text().toInteger()
	println "Landing page links to ${lastPageNumber} pages with topics"

	(524..lastPageNumber).each() {

		currentPageNumber ->
		println "\tProcessing page ${currentPageNumber} of ${lastPageNumber}"

		go "http://forum.viva.nl/forum/Gezondheid/list_topics/6?data[page]=${currentPageNumber}"
		assert title == "Viva - Onderwerpen van forum Gezondheid"

		def topicList = $("table tbody td.topic-name")
		assert topicList.size() > 1
		println "\tPage links to ${topicList.size()} topics"

		topicList.eachWithIndex() {

			topic, i ->
			println "\t\t${i}: ${topic.find("a.topic-link").text()}" 

		}

	}

}

/*
<dl class="discussion-navigation page-navigation before">
<dt>Pagina's:</dt>
<dd>
<a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=1" class="current-page">1</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=2">2</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=3">3</a> <span class="cutoff">&#8230;</span><a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=262">262</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=263">263</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=264">264</a> <span class="cutoff">&#8230;</span><a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=524">524</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=525">525</a> <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=526">526</a> - <a href="http://forum.viva.nl/forum/Gezondheid/list_topics/6?data%5Bpage%5D=2" rel="next">Volgende</a>
</dd>

http://forum.viva.nl/forum/Gezondheid/list_topics/6?data[page]=1
*/