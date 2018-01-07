package com.example.lele.protoui;

/**
 * Created by TeeKee on 2017/12/20.
 */

public class GetClassifify {
    private float mean = 0;
    private float var = 0;
    private float std = 0;
    private float max = 0;
    private float min = 0;
    private float kurt = 0;
    private float skew = 0;
    private float rms = 0;

    final String W = "Walking";
    final String U = "Upstairs";
    final String D = "Downstairs";
    final String J = "Jogging";
    final String SI = "Sitting";
    final String ST = "Standing";

    String activity_online(float[] feature) {
        String activity = "Standing";
        save_feature(feature);
        activity = model();
        return activity;
    }

    void save_feature(float[] feature) {
        mean = feature[0];
        var = feature[1];
        std = feature[2];
        max = feature[3];
        min = feature[4];
        kurt = feature[5];
        skew = feature [6];
        rms = feature[7];
    }

    String model() {
        String activity = ST;
        if(std <= 0.84) {
            if(std > 0.09) {
                activity = ST;
            }
            else if(std <= 0.09) {
                if(std <= 0.02){
                    activity = SI;
                }
                else if(std > 0.02) {
                    if(max <= 9.6) {
                        if(mean <= 9.47) {
                            activity = SI;
                        }
                        else if(mean > 9.47) {
                            activity = ST;
                        }
                    }
                    else if(max > 9.6){
                        if(max <= 9.83) {
                            activity = SI;
                        }
                        else if(max > 9.83) {
                            if(min <= 9.8) {
                                if(min <= 9.62) {
                                    activity = SI;
                                }
                                else if(min >9.62) {
                                    activity = ST;
                                }
                            }
                            else if(min > 9.8){
                                if(mean <= 10.01) {
                                    activity = SI;
                                }
                                else if(mean > 10.01) {
                                    activity = ST;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(std > 0.84) {
            if(var > 33.29) {
                if (var > 38.76) {
                    activity = J;
                }
                else if(var < 38.76){
                    if(rms > 13.94) {
                        activity = J;
                    }
                    else if(rms <= 13.94) {
                        if(kurt <= 1.95) {
                            activity = J;
                        }
                        else if(kurt > 1.95) {
                            if(mean <= 11.15) {
                                activity = J;
                            }
                            else if(mean > 11.15) {
                                activity = W;
                            }
                        }
                    }
                }
            }
            else if(var <= 33.29) {
                if(rms > 13.75) {
                    if(std <= 4.63) {
                        activity = D;
                    }
                    else if(std > 4.63) {
                        activity = J;
                    }
                }
                else if(rms <= 13.75) {
                    activity = rms_less_13p75();
                }
            }
        }
        return activity;
    }

    String rms_less_13p75() {
        String activity = ST;
        if(mean <= 10.31) {
            if(skew <= 0.21) {
                activity = U;
            }
            else if(skew > 0.21) {
                if(mean > 10.16) {
                    if(kurt <= 2.51) {
                        activity = W;
                    }
                    else if(kurt > 2.51) {
                        activity = U;
                    }
                }
                else if(mean <= 10.16) {
                    if(std > 4.64) {
                        activity = D;
                    }
                    else if(std <= 4.64) {
                        if(max > 21.38) {
                            activity = U;
                        }
                        else if(max <= 21.38) {
                            if(skew > 0.6) {
                                activity = D;
                            }
                            else if(skew <= 0.6) {
                                if(min <= 2.8) {
                                    activity = D;
                                }
                                else if(min > 2.8) {
                                    activity = U;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(mean > 10.31) {
            if(max <= 18.3) {
                if(rms <= 10.74) {
                    activity = U;
                }
                else if(rms > 10.74) {
                    if(kurt <= 2.3) {
                        activity = W;
                    }
                    else if(kurt > 2.3) {
                        if(skew <= -0.11) {
                            activity = W;
                        }
                        else if(skew > -0.11) {
                            if(min <= 4.61) {
                                activity = U;
                            }
                            else if(min > 4.61) {
                                activity = W;
                            }
                        }
                    }
                }
            }
            else if(max > 18.3) {
                if(std > 5.03) {
                    if(rms > 12.3) {
                        activity = W;
                    }
                    else if(rms <= 12.3) {
                        if(min <= 2.03) {
                            activity = D;
                        }
                        else if(min > 2.03) {
                            if(mean <= 10.79) {
                                activity = D;
                            }
                            else if(mean > 10.79) {
                                activity = W;
                            }
                        }
                    }
                }
                else if(std <= 5.03) {
                    activity = std_less_5p03();
                }
            }
        }
        return activity;
    }

    String std_less_5p03() {
        String activity = ST;
        if(min <= 2.39) {
            activity = U;
        }
        else if(min > 2.39) {
            if(mean > 11.33) {
                if(var > 22.86) {
                    activity = D;
                }
                else if(var <= 22.86) {
                    if(min <= 3.13) {
                        activity = U;
                    }
                    else if(min > 3.13) {
                        activity = D;
                    }
                }
            }
            else if(mean <= 11.33) {
                if(rms <= 11.01){
                    activity = U;
                }
                else if(rms > 11.01) {
                    if(min > 3.81) {
                        activity = W;
                    }
                    else if(min <= 3.81) {
                        if(max > 21.41) {
                            if(skew <= 0.66) {
                                activity = W;
                            }
                            else if(skew > 0.66) {
                                activity = U;
                            }
                        }
                        else if(max <= 21.41) {
                            if(kurt > 2.34) {
                                activity = U;
                            }
                            else if(kurt <= 2.34) {
                                if(var <= 21.05) {
                                    activity = W;
                                }
                                else if(var > 21.05) {
                                    activity = D;
                                }
                            }
                        }
                    }
                }
            }
        }
        return activity;
    }
}
