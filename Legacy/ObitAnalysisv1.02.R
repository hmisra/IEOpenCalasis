library(openNLP)
library(tm)
library(SnowballC)
library(RColorBrewer)
library(scales)
library(wordcloud)
library(devtools)
library(qdap)
library(tm.plugin.sentiment)
require(tm.plugin.webmining)
library(RTextTools)
library(NLP)

create_matrix1 <- function(textColumns, language="english", minDocFreq=1, maxDocFreq=Inf, minWordLength=3, maxWordLength=Inf, ngramLength=1, originalMatrix=NULL, removeNumbers=FALSE, removePunctuation=TRUE, removeSparseTerms=0, removeStopwords=TRUE,  stemWords=FALSE, stripWhitespace=TRUE, toLower=TRUE, weighting=weightTf) {
  
  stem_words <- function(x) {
    split <- strsplit(x," ")
    return(wordStem(unlist(split),language=language))
  }
  
  tokenize_ngrams <- function(x, n=ngramLength) return(rownames(as.data.frame(unclass(textcnt(x,method="string",n=n)))))
  
  control <- list(bounds=list(local=c(minDocFreq,maxDocFreq)),language=language,tolower=toLower,removeNumbers=removeNumbers,removePunctuation=removePunctuation,stopwords=removeStopwords,stripWhitespace=stripWhitespace,wordLengths=c(minWordLength,maxWordLength),weighting=weighting)
  
  if (ngramLength > 1) { 
    control <- append(control,list(tokenize=tokenize_ngrams),after=7)
  } else {
    control <- append(control,list(tokenize=scan_tokenizer),after=4)
  }
  
  if (stemWords == TRUE) control <- append(control,list(stemming=stem_words),after=7)
  
  trainingColumn <- apply(as.matrix(textColumns),1,paste,collapse=" ")
  trainingColumn <- sapply(as.vector(trainingColumn,mode="character"),iconv,to="UTF8",sub="byte")
  
  corpus <- Corpus(VectorSource(trainingColumn),readerControl=list(language=language))
  matrix <- DocumentTermMatrix(corpus,control=control);
  if (removeSparseTerms > 0) matrix <- removeSparseTerms(matrix,removeSparseTerms)
  
  if (!is.null(originalMatrix)) {
    terms <- colnames(originalMatrix[,which(!colnames(originalMatrix) %in% colnames(matrix))])
    
    weight <- 0
    if (attr(weighting,"acronym")=="tf-idf") weight <- 0.000000001
    amat <- matrix(weight,nrow=nrow(matrix),ncol=length(terms))
    colnames(amat) <- terms
    rownames(amat) <- rownames(matrix)
    
    fixed <- as.DocumentTermMatrix(cbind(matrix[,which(colnames(matrix) %in% colnames(originalMatrix))],amat),weighting=weighting)
    matrix <- fixed
  }
  
  matrix <- matrix[,sort(colnames(matrix))]
  
  gc()
  return(matrix)
}


setwd('/Users/himanshumisra/Desktop/Legacy/')
df<-read.csv("./path/data.csv", header = FALSE, sep = ",", quote="\"")
names(df)<-c("text", "label")
df$text<-as.character(df$text)
df$label<-factor(df$label)

ServiceCorpus=Corpus(VectorSource(df$text))
ServiceCorpus <- tm_map(ServiceCorpus, content_transformer(tolower))
ServiceCorpus <- tm_map(ServiceCorpus, removePunctuation)
ServiceCorpus <- tm_map(ServiceCorpus, removeWords, c("june", "sunday", "monday", "tuesday","wednesday", "thursday", "friday", "saturday", "2015", "january", "feburary", "march", "august", "april", "may", "july", "september", "october", "november", "december"))
ServiceCorpus <- tm_map(ServiceCorpus, removeWords, stopwords("english"))
ServiceCorpus <- tm_map(ServiceCorpus, stripWhitespace)
ServiceCorpus <- tm_map(ServiceCorpus, removeNumbers)
dtm <- DocumentTermMatrix(ServiceCorpus)
dtms<- removeSparseTerms(dtm,0.995)


container <- create_container(dtms, df$label, trainSize=1:5000,testSize=6001:6680, virgin=FALSE)
SVM <- train_model(container,"SVM")
GLMNET <- train_model(container,"GLMNET")
MAXENT <- train_model(container,"MAXENT")
SLDA <- train_model(container,"SLDA")
BOOSTING <- train_model(container,"BOOSTING")
BAGGING <- train_model(container,"BAGGING")
RF <- train_model(container,"RF")
NNET <- train_model(container,"NNET")
TREE <- train_model(container,"TREE")



allfiles<-list.files(path="/Users/himanshumisra/Desktop/Legacy/test")
setwd("/Users/himanshumisra/Desktop/Legacy/test")
for (file in allfiles)
{
  text<-readChar(file, file.info(file)$size)
  sentences<-sent_detect_nlp(text)
  predMatrix<-create_matrix1(sentences, originalMatrix = dtms)
  predSize = length(sentences);
  predictionContainer <- create_container(predMatrix, labels=rep(0,predSize), testSize=1:predSize, virgin=FALSE)
  
  SVM_CLASSIFY <- classify_model(predictionContainer, SVM)
  GLMNET_CLASSIFY <- classify_model(predictionContainer, GLMNET)
  MAXENT_CLASSIFY <- classify_model(predictionContainer, MAXENT)
  SLDA_CLASSIFY <- classify_model(predictionContainer, SLDA)
  BOOSTING_CLASSIFY <- classify_model(predictionContainer, BOOSTING)
  BAGGING_CLASSIFY <- classify_model(predictionContainer, BAGGING)
  RF_CLASSIFY <- classify_model(predictionContainer, RF)
  NNET_CLASSIFY <- classify_model(predictionContainer, NNET)
  TREE_CLASSIFY <- classify_model(predictionContainer, TREE)
  

  classification<-cbind(SVM_CLASSIFY, GLMNET_CLASSIFY, MAXENT_CLASSIFY, SLDA_CLASSIFY, BOOSTING_CLASSIFY, BAGGING_CLASSIFY, RF_CLASSIFY, NNET_CLASSIFY, TREE_CLASSIFY)
  classification$sentence<- sentences
  a<-file.path("..", "Output", file)
  write.csv(classification, a)
  
}


