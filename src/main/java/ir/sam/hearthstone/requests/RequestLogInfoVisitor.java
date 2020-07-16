package ir.sam.hearthstone.requests;

public interface RequestLogInfoVisitor {
    void setAddCardToDeck(AddCardToDeck addCardToDeck);

    void setBuyCard(BuyCard buyCard);

    void setChangeDeckName(ChangeDeckName changeDeckName);

    void setChangeHeroDeck(ChangeHeroDeck changeHeroDeck);

    void setCollectionDetails(CollectionFilter collectionFilter);

    void setDeleteAccount(DeleteAccount deleteAccount);

    void setDeleteDeck(DeleteDeck deleteDeck);

    void setEndTurn(EndTurn endTurn);

    void setExitGame(ExitGame exitGame);

//    void setFirstCollection(FirstCollection firstCollection);

    void setLoginRequest(LoginRequest loginRequest);

    void setLogoutRequest(LogoutRequest logoutRequest);

    void setNewDeck(NewDeck newDeck);

    void setPlayCard(SelectCardInHand selectCardInHand);

    void setRemoveCardFromDeck(RemoveCardFromDeck removeCardFromDeck);

    void setSelectPassive(SelectPassive selectPassive);

    void setSellCard(SellCard sellCard);

    void setShopRequest(ShopRequest shopRequest);

    void setStartPlaying(StartPlaying startPlaying);

    void setStatus(StatusRequest statusRequest);

    void setShutdownRequest(ShutdownRequest shutdownRequest);
}
