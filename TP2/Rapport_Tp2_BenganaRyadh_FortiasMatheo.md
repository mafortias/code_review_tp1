---
author: Bengana Ryadh & Fortias Mathéo  
date: 2024-12-17  
title: Rapport - TP2 Security Corporate  
---

# Rapport - TP2 Security Corporate

_Auteurs : Bengana Ryadh & Fortias Mathéo_  
_Date : 2024-12-17_

**com.michelin.ACO.crypto-2.0.0.jar** correspond à la librairie à analyser. Il faut la décompiler avec la commande :
```bash
$ java -jar jd-gui-1.6.6.jar com.michelin.ACO.crypto-2.0.0.jar
```

## Comment retrouver le secret ?

Après avoir décompilé la librairie, on accède à la classe `SimpleCryptoHandler.class`. Nous avons remarqué que la classe `mC` est utilisée pour l'attribut `facade`, en particulier sa méthode statique `a()`. Cette méthode a donc retenu notre attention. Elle prend en paramètres deux chaînes de caractères, `paramString1` et `paramString2`, et initialise l'objet `facade`.

Dans le constructeur de la classe `mC`, la méthode `a()` de l'attribut `z` (une instance de `mD`) est appelée. Cette méthode reçoit les mêmes paramètres que la méthode `a()` précédente. Elle retourne une clé, laquelle est ensuite stockée dans l'attribut `B` de l'objet `facade`. En analysant cette méthode, nous avons constaté un appel à une autre méthode, `b()`. Cette dernière ouvre le KeyStore. Nous en avons déduit que `paramString1` correspond au KeyStore et que `paramString2` (nommée `str` dans le fichier `SimpleCryptoHandler.class`) est le mot de passe.

En revenant à l’analyse de `SimpleCryptoHandler.class`, nous avons conclu que retrouver le mot de passe revient à déterminer la valeur de `str`.

Après avoir copié le code de la classe `SimpleCryptoHandler.java` dans un fichier `java` et corrigé les erreurs de syntaxe dues à la décompilation, nous avons affiché le contenu de la chaîne `str`. Cela nous a permis d’obtenir le secret ou mot de passe suivant : `aM#2uT)@e1*A`.

Pour ouvrir le KeyStore, nous utilisons la commande suivante :  
```bash
$ keytool -list -keystore Secret.ks
```
Puis nous rentrons le mot de passe obtenu précédemment.

## Comment fonctionne le chiffrement du mot de passe ?

Le chiffrement fonctionne de la manière suivante. On part de la méthode ```encrypt()``` du fichier `SimpleCryptoHandler.java`. Dans celle-ci, la méthode ```a()``` de la classe ```mC``` est appelée. Elle prend en paramètre un objet. Un tableau de bytes est créé,  contenant l'encodage UTF-8 de ```l'objet:"son_type"```. Il est ensuite chiffrer en faisant appel à la méthode ```c()``` de ```z```, ```z``` est une instance de la classe ```mD```. Cette méthode fait ensuite appel à une autre méthode, la méthode ```b()```, qui réalise un chiffrement AES-CBC puis encode en base64.