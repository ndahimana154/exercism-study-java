public class Lasagna {
    // TODO: define the 'expectedMinutesInOven()' method
public int expectedMinutesInOven () { 
    return 40;
}
    // TODO: define the 'remainingMinutesInOven()' method
public int remainingMinutesInOven(int howLong) {
    return expectedMinutesInOven() - howLong;
}
    // TODO: define the 'preparationTimeInMinutes()' method
public int preparationTimeInMinutes(int layersNum) {
    return layersNum*2;
}
    // TODO: define the 'totalTimeInMinutes()' method
    public int totalTimeInMinutes(int layersNum,int minNum) {
        return preparationTimeInMinutes(layersNum) + minNum;
    }
}
