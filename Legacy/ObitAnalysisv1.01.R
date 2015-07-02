install.packages('tm')
install.packages('NLP')
install.packages('SnowballC')
install.packages('RColorBrewer')
install.packages('scales')
install.packages('RGraphviz')
install.packages('wordcloud')
install.packages('devtools')
install.packages("tm.plugin.webmining")
install.packages("RTextTools")
install.packages("openNLP")
install.packages("qdap")
library(openNLP)
library(tm)
library(SnowballC)
library(RColorBrewer)
library(scales)
library(wordcloud)
library(devtools)
library(qdap)
install_github("mannau/tm.plugin.sentiment")
library(tm.plugin.sentiment)
require(tm.plugin.webmining)
library(RTextTools)
library(NLP)


setwd('/Users/himanshumisra/Desktop/Legacy/')

filePathService=file.path(".", "service")
filePathEducation=file.path(".", "education")
filePathMilitary=file.path(".", "military")
filePathNoCat=file.path(".", "nocat")

df<-read.csv("./path/data.csv", header = FALSE, sep = ",", quote="\"")
names(df)<-c("text", "label")
df$text<-as.character(df$text)
df$label<-factor(df$label)
str(df)

ServiceCorpus=Corpus(VectorSource(df$text))

ServiceCorpus <- tm_map(ServiceCorpus, content_transformer(tolower))
ServiceCorpus <- tm_map(ServiceCorpus, removePunctuation)
ServiceCorpus <- tm_map(ServiceCorpus, removeWords, c("june", "sunday", "monday", "tuesday","wednesday", "thursday", "friday", "saturday", "2015", "january", "feburary", "march", "august", "april", "may", "july", "september", "october", "november", "december"))
ServiceCorpus <- tm_map(ServiceCorpus, removeWords, stopwords("english"))
ServiceCorpus <- tm_map(ServiceCorpus, stripWhitespace)
ServiceCorpus <- tm_map(ServiceCorpus, removeNumbers)






#ObitCorpus <- tm_map(ObitCorpus, stemDocument)
dtm <- DocumentTermMatrix(ServiceCorpus)
dtm
freq <- colSums(as.matrix(dtm))
ord <- order(freq)
freq[tail(ord)]
head(table(freq), 15)
tail(table(freq), 15)

dim(dtm)
dtms<- removeSparseTerms(dtm,0.995)

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





# SVM_CLASSIFY <- classify_model(container, SVM)
# GLMNET_CLASSIFY <- classify_model(container, GLMNET)
# MAXENT_CLASSIFY <- classify_model(container, MAXENT)
# SLDA_CLASSIFY <- classify_model(container, SLDA)
# BOOSTING_CLASSIFY <- classify_model(container, BOOSTING)
# BAGGING_CLASSIFY <- classify_model(container, BAGGING)
# RF_CLASSIFY <- classify_model(container, RF)
# NNET_CLASSIFY <- classify_model(container, NNET)
# TREE_CLASSIFY <- classify_model(container, TREE)

# 
# 
# convert_text_to_sentences <- function(text, lang = "en") {
#   sentence_token_annotator <- Maxent_Sent_Token_Annotator(language = lang)
#   
#   text <- as.character(text)
#   
#   sentence.boundaries <- annotate(text, sentence_token_annotator)
#   sentence.boundaries
#   
#   str(sentence.boundaries)
#   sentences <- text[sentence.boundaries]
#   
#   
# }

# 
# analytics <- create_analytics(container,cbind(SVM_CLASSIFY, SLDA_CLASSIFY,BOOSTING_CLASSIFY,RF_CLASSIFY, GLMNET_CLASSIFY,NNET_CLASSIFY, TREE_CLASSIFY,MAXENT_CLASSIFY))
# summary(analytics)
# 
# create_ensembleSummary(analytics@document_summary)



# RESULTS WILL BE REPORTED BACK IN THE analytics VARIABLE.
# analytics@algorithm_summary: SUMMARY OF PRECISION, RECALL, F-SCORES, AND ACCURACY SORTED BY TOPIC CODE FOR EACH ALGORITHM
# analytics@label_summary: SUMMARY OF LABEL (e.g. TOPIC) ACCURACY
# analytics@document_summary: RAW SUMMARY OF ALL DATA AND SCORING
# analytics@ensemble_summary: SUMMARY OF ENSEMBLE PRECISION/COVERAGE. USES THE n VARIABLE PASSED INTO create_analytics()
