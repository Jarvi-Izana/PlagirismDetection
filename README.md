# Caveat
* For more detail about the program, you can refer to my detailed comments in the program.

* program will only accept input under current path.

# Commands
## built in
* make

* make test # run the test on the test sets

* make run # run on the sample input

## customized input
Use the **Client** class to accept customized input

        java Client synonyms.txt source.txt target.txt 4

# Program Structure
## interfaces
        Synonymsor Indexable
## general class to implement the algorithm
        PlagiarismDetection -> accept objects implement interfaces above
## Client
        Used to run PlagiarismDetection instance.

# Assumptions
## synonyms input
I assume one line is for one synonyms group.
If multiple lines are exactly same, I treat them different because they may have different semantics based on different context.

For instance:   
A B   
A B   
I treat them as different group with different meanings.    

When checking the matching pattern of N tuple, I **ignore semantics** for a word.   
For instance:    
synonyms groups:    
A V F, meaning 1   
A K, meaning 2   
sentence: A B C.    
In this context, A should mean 1. However, I would also check group (A K), to exhaust all possible N tuple matching.

## source and target input
I assume **all** input file are separated by whitespace. In other words, I don't take punctuations into account. Based on this, I create an interface to handle general input. I call this model as abstract file.

However, it's easy to model the situation when taking punctuations into account. Based on my abstract file model, you can view a file consists of many abstracted files, separated by punctuations.

For instance:   
[abstract file] [punctuation] [abstract file] [punctuation]

# Abstract File Model
I model abstract file as an indexbale list of certain objects. In some aspects, it's a similar concept to index on an array.

For regular file, it can be view as a series of **string words**, that is, [string word] ... [string word]. Similarly, for an arbitrary abstract file, it can be viewed as [E] ... [E], where E is a single element or object of that abstract file.

## Indexable interface
```java
public interface Indexable<E extends Comparable<E>> {
    E get(int i);
    int size();
    int currentIdx();
    int resetIdx();
}

```
Class **Text** implements this interface to index on single word. This class is based on regular file.

```java
public class Text implements Indexable<String>, Iterable<String>
```

For other forms of file such as webpage, one can **reuse** the plagiarism detection code by simply implementing this interface.

## Synonymsor interface
```java
public interface Synonymsor<K extends Comparable<K>, V extends Comparable<V>> {
    /* Key -> unique ids */
    Map<K, List<V>> getSynonyms();

    /* group id -> all the synonyms belong to this group */
    Map<V, List<K>> getInversedSyno();
}
```

This interface is used for an abstract file to provide method to check synonyms.

```java
public class Synonyms implements Synonymsor<String, Integer>
```

Class **Synonyms** implements this interface. This abstract file is backed by string file.

# Algorithm
## Fast Access
First, I load the source input, and map all its element to its occurrence in this source.

```java
private Map<E, List<Integer>> fastAccess(Indexable<E> source)
```
For instance:
Abstract file: A B C A B A C E Y U   
Pattern to match: A    
FastAccess: A -> 0 3 4    
Instead compared with certain tuple by scanning the whole file, use this map to find the index to start a comparison.

## Unique group id
```java
Map<K, List<V>> getSynonyms();
```

For each synonyms group, I will map them with a unique id. When two E have synonyms, I will match on all the possibility to check a match.

For instance:    
A B 1    
A C D 2    

build mapping as follow:    
A -> 1, 2    
C -> 2

## Detection on N tuple
### not synonyms
Just check whether current two elements are equal. If true, match on the following elements.

### synonyms
When comparing A and C, first get unique id respectively, then check whether they have same id, meaning in the same synonyms group.

For more detail, please see detection method at PlagiarismDetection. I have comments for further explanation.
```java
private boolean detection(Indexable<E> source, Indexable<E> target, int sidx, int tidx, int N)
```

# Discussion on other algorithm
One may think about KMP.

Source: A A A A A A A A A B

target: A A A C

For KMP, this case may be better, because there are many duplicate in source pattern. The pattern matching complexity is worst at **O(P\*(M + N))**.

P is number of the patterns, that is, number of N tuple in target. M is the length of source and N is the length of tuple. While in this case, my complexity of my implementation is **O(P\*M*N)**.

However, in real case, we detect on **rich content** files (real world, maybe theses), that is, the file should not contain too many duplicates and different words are sparsely distributed in file.

My algorithm should work **better** under above scenario on average, **O(P\*m\*N)**, where **m** is average # of indexes in source to start a detection, and **m << M, m\*N < M**.
