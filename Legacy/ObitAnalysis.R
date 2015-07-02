install.packages('tm')
install.packages('NLP')
install.packages('SnowballC')
install.packages('RColorBrewer')
install.packages('scales')
install.packages('RGraphviz')
install.packages('wordcloud')
install.packages('devtools')
install.packages("tm.plugin.webmining")
library(tm)
library(SnowballC)
library(RColorBrewer)
library(scales)
library(wordcloud)
library(devtools)
install_github("mannau/tm.plugin.sentiment")
library(tm.plugin.sentiment)
require(tm.plugin.webmining)

setwd('/Users/himanshumisra/Desktop/Legacy/')
filePathObit=file.path(".", "obit")
length(dir(filePathObit))

ObitCorpus=Corpus(DirSource(filePathObit))
ObitCorpus$documents




ObitCorpus <- tm_map(ObitCorpus, content_transformer(tolower))
ObitCorpus <- tm_map(ObitCorpus, removePunctuation)
ObitCorpus <- tm_map(ObitCorpus, removeWords, c("june", "sunday", "monday", "tuesday","wednesday", "thursday", "friday", "saturday", "2015", "january", "feburary", "march", "august", "april", "may", "july", "september", "october", "november", "december"))
ObitCorpus <- tm_map(ObitCorpus, removeWords, stopwords("english"))
ObitCorpus <- tm_map(ObitCorpus, stripWhitespace)
ObitCorpus <- tm_map(ObitCorpus, removeNumbers)

dataframe<-data.frame(text=unlist(sapply(ObitCorpus, `[`, "content")), stringsAsFactors=F)
dataframe$text
dataframe$label<-'service'
head(dataframe)




#ObitCorpus <- tm_map(ObitCorpus, stemDocument)
dtm <- DocumentTermMatrix(ObitCorpus)
freq <- colSums(as.matrix(dtm))
ord <- order(freq)
freq[tail(ord)]
head(table(freq), 15)
tail(table(freq), 15)

dim(dtm)
dtms<- removeSparseTerms(dtm,0.9)
dim(dtms)
freq <- colSums(as.matrix(dtms))
ord <- order(freq)
freq[tail(ord,100)]
head(table(freq), 15)
tail(table(freq), 15)

#Rgraphviz error
#plot(dtms, terms=findFreqTerms(dtm, lowfreq=100)[1:50],corThreshold=0.5)

set.seed(123)
wordcloud(names(freq), freq, min.freq=10, max.words = 100, colors=brewer.pal(6, "Dark2"), rot.per = 0.2)


ObitCorpus <- score(ObitCorpus)

sentixts <- metaXTS(ObitCorpus)
sentixts
chartSentiment(sentixts )


