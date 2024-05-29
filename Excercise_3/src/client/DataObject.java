package client;

public class DataObject {
    private final int _tsId;
    private final String _data;
    private final int _lsn;

    public DataObject(int tsid, String data, int lsn) {
        _tsId = tsid;
        _data = data;
        _lsn = lsn;
    }

    public int getTransactionId() {
        return _tsId;
    }

    public String getData() {
        return _data;
    }

    public int getLSN() {return _lsn;}

    @Override
    public String toString() {
        return "Write <" + _data + "> for Transaction <" + _tsId + ">";
    }
}
