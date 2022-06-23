# This script subscribes to the Digital Twin's SCADA to receive state values
# Then it publishes these states to Unity's Remote HMI

import time
import sys
import zmq
import json

# Socket to talk to server
CONTEXT = zmq.Context()

# TWIN refers to Digital Twin
TWIN_PORT = "5876"

# TWIN_IP = 'tcp://localhost:'
#TWIN_IP = 'tcp://192.168.1.155:'
#TWIN_IP = 'tcp://10.107.124.140:'
#TWIN_IP = 'tcp://192.168.43.240:'
#TWIN_IP = 'tcp://10.0.1.177:'
#TWIN_IP = 'tcp://25.50.165.204:'
#TWIN_IP = 'tcp://25.72.150.14:'
#TWIN_IP = 'tcp://25.29.47.66:'
#TWIN_IP = 'tcp://25.30.221.113:'
#TWIN_IP = 'tcp://10.26.7.200:'
TWIN_IP = 'tcp://192.168.2.200:'
# TWIN_IP = 'tcp://10.26.25.200:'


# Subscribe to the twin's incoming message
TWIN_SUBSCRIBE_SOCKET = CONTEXT.socket(zmq.SUB)
TWIN_SUBSCRIBE_SOCKET.connect(TWIN_IP + TWIN_PORT)
#topicFilterSWatState="SWaT"+"!"
TWIN_SUBSCRIBE_TOPIC="Extract"+"!"

TWIN_SUBSCRIBE_SOCKET.setsockopt_string(zmq.SUBSCRIBE, TWIN_SUBSCRIBE_TOPIC)
TWIN_SUBSCRIBE_SOCKET.setsockopt(zmq.RCVTIMEO, 5000)
# TWIN_SUBSCRIBE_SOCKET.setsockopt(zmq.RCVHWM, 100)
# TWIN_SUBSCRIBE_SOCKET.setsockopt(zmq.SNDTIMEO, 500)
# TWIN_SUBSCRIBE_SOCKET.setsockopt(zmq.LINGER, 0)


# Publish the received response to Unity's HMI
PythonUnityBridge_PUBLISH_SOCKET = CONTEXT.socket(zmq.PUB)
PythonUnityBridge_PUBLISH_SOCKET.bind("tcp://*:12347")
# PythonUnityBridge_PUBLISH_SOCKET.setsockopt(zmq.RCVTIMEO, 500)
# PythonUnityBridge_PUBLISH_SOCKET.setsockopt(zmq.SNDTIMEO, 500)
# PythonUnityBridge_PUBLISH_SOCKET.setsockopt(zmq.LINGER, 0)

def component_status(component_name, argus_value, historian_value, sensor_bias, tolerated_difference):
    
    discrepancy = float(argus_value) + sensor_bias - float(historian_value)

    if abs(discrepancy) < abs(tolerated_difference):
        return "normal"
    else:
        print(component_name, argus_value, historian_value)
        return "attack"

def handle_status(status):
    if status == "Running" or status == "Open":
        return 2
    elif status == "Stopped" or status == "Closed":
        return 1
    elif status == "Inactive":
        return 3
    elif status == "null" or status is None:
        # print("Exceptional status: " + str(status))
        return 0
    elif status == "Manual":
        return '*'
    else:
        if(isinstance(status, float)):
            return float("{:.2f}".format(status))
        else:
            return status

def processPlantState(twin_state_dictionary):

        # DT_Exercise = str(twin_state_dictionary["Exercise"])
        DT_Exercise = "LS"
        DT_Version = str(twin_state_dictionary["Version"]).replace(" ", "_")
        DT_HMI_Mode  = str(twin_state_dictionary["HMI1"])
        DT_HMI_Mode2 = str(twin_state_dictionary["HMI2"])
        DT_HMI_Mode3 = str(twin_state_dictionary["HMI3"])
        try:
            DT_HMI_Mode4 = str(twin_state_dictionary["HMI4"])
            DT_HMI_Mode5 = str(twin_state_dictionary["HMI5"])
        except:
            DT_HMI_Mode4 = '0'
            DT_HMI_Mode5 = '0'

        DT_Control = str(twin_state_dictionary["Command"])
        DT_HMI_In_Control = str(twin_state_dictionary["HMIInControl"])
        DT_Terminate = str(twin_state_dictionary["Terminate"])
        DT_LIT101 = str(handle_status(twin_state_dictionary["LIT101"]))
        DT_MV101 = str(handle_status(twin_state_dictionary["MV101"]))
        DT_FIT101 = str(handle_status(twin_state_dictionary["FIT101"]))
        DT_P101 = str(handle_status(twin_state_dictionary["P101"]))
        DT_P102 = str(handle_status(twin_state_dictionary["P102"]))
        DT_FIT201 = str(handle_status(twin_state_dictionary["FIT201"]))
        DT_AIT201 = str(handle_status(twin_state_dictionary["AIT201"]))
        DT_AIT202 = str(handle_status(twin_state_dictionary["AIT202"]))
        DT_AIT203 = str(handle_status(twin_state_dictionary["AIT203"]))
        DT_P201 = str(handle_status(twin_state_dictionary["P201"]))
        DT_P202 = str(handle_status(twin_state_dictionary["P202"]))
        DT_P203 = str(handle_status(twin_state_dictionary["P203"]))
        DT_P204 = str(handle_status(twin_state_dictionary["P204"]))
        DT_P205 = str(handle_status(twin_state_dictionary["P205"]))
        DT_P206 = str(handle_status(twin_state_dictionary["P206"]))
        DT_MV201 = str(handle_status(twin_state_dictionary["MV201"]))
        DT_AIT301 = str(handle_status(twin_state_dictionary["AIT301"]))
        DT_AIT302 = str(handle_status(twin_state_dictionary["AIT302"]))
        DT_AIT303 = str(handle_status(twin_state_dictionary["AIT303"]))
        DT_DPIT301 = str(handle_status(twin_state_dictionary["DPIT301"]))
        DT_FIT301 = str(handle_status(twin_state_dictionary["FIT301"]))
        DT_LIT301 = str(handle_status(twin_state_dictionary["LIT301"]))
        DT_MV301 = str(handle_status(twin_state_dictionary["MV301"]))
        DT_MV302 = str(handle_status(twin_state_dictionary["MV302"]))
        DT_MV303 = str(handle_status(twin_state_dictionary["MV303"]))
        DT_MV304 = str(handle_status(twin_state_dictionary["MV304"]))
        DT_P301 = str(handle_status(twin_state_dictionary["P301"]))
        DT_P302 = str(handle_status(twin_state_dictionary["P302"]))

        DT_LIT201 = str(handle_status(twin_state_dictionary["LIT201"]))
        DT_LIT202 = str(handle_status(twin_state_dictionary["LIT202"]))
        DT_LIT203 = str(handle_status(twin_state_dictionary["LIT203"]))

        DT_FIT101MarkersL = str(handle_status(twin_state_dictionary["FIT101Markers"]['L']))
        DT_FIT101MarkersH = str(handle_status(twin_state_dictionary["FIT101Markers"]['H']))
        DT_FIT201MarkersL = str(handle_status(twin_state_dictionary["FIT201Markers"]['L']))
        DT_FIT201MarkersH = str(handle_status(twin_state_dictionary["FIT201Markers"]['H']))
        DT_LIT101MarkersLL= str(handle_status(twin_state_dictionary["LIT101Markers"]['LL']))
        DT_LIT101MarkersL = str(handle_status(twin_state_dictionary["LIT101Markers"]['L']))
        DT_LIT101MarkersH = str(handle_status(twin_state_dictionary["LIT101Markers"]['H']))
        DT_LIT101MarkersHH= str(handle_status(twin_state_dictionary["LIT101Markers"]['HH']))
        DT_LIT201MarkersLL= str(handle_status(twin_state_dictionary["LIT201Markers"]['LL']))
        DT_LIT201MarkersL = str(handle_status(twin_state_dictionary["LIT201Markers"]['L']))
        DT_LIT201MarkersH = str(handle_status(twin_state_dictionary["LIT201Markers"]['H']))
        DT_LIT201MarkersHH= str(handle_status(twin_state_dictionary["LIT201Markers"]['HH']))
        DT_LIT202MarkersLL= str(handle_status(twin_state_dictionary["LIT202Markers"]['LL']))
        DT_LIT202MarkersL = str(handle_status(twin_state_dictionary["LIT202Markers"]['L']))
        DT_LIT202MarkersH = str(handle_status(twin_state_dictionary["LIT202Markers"]['H']))
        DT_LIT202MarkersHH= str(handle_status(twin_state_dictionary["LIT202Markers"]['HH']))
        DT_LIT203MarkersLL= str(handle_status(twin_state_dictionary["LIT203Markers"]['LL']))
        DT_LIT203MarkersL = str(handle_status(twin_state_dictionary["LIT203Markers"]['L']))
        DT_LIT203MarkersH = str(handle_status(twin_state_dictionary["LIT203Markers"]['H']))
        DT_LIT203MarkersHH= str(handle_status(twin_state_dictionary["LIT203Markers"]['HH']))
        DT_AIT201MarkersL = str(handle_status(twin_state_dictionary["AIT201Markers"]['L']))
        DT_AIT201MarkersH = str(handle_status(twin_state_dictionary["AIT201Markers"]['H']))
        DT_AIT202MarkersL = str(handle_status(twin_state_dictionary["AIT202Markers"]['L']))
        DT_AIT202MarkersH = str(handle_status(twin_state_dictionary["AIT202Markers"]['H']))
        DT_AIT203MarkersL = str(handle_status(twin_state_dictionary["AIT203Markers"]['L']))
        DT_AIT203MarkersH = str(handle_status(twin_state_dictionary["AIT203Markers"]['H']))
        DT_FIT301MarkersL = str(handle_status(twin_state_dictionary["FIT301Markers"]['L']))
        DT_FIT301MarkersH = str(handle_status(twin_state_dictionary["FIT301Markers"]['H']))
        DT_LIT301MarkersLL= str(handle_status(twin_state_dictionary["LIT301Markers"]['LL']))
        DT_LIT301MarkersL = str(handle_status(twin_state_dictionary["LIT301Markers"]['L']))
        DT_LIT301MarkersH = str(handle_status(twin_state_dictionary["LIT301Markers"]['H']))
        DT_LIT301MarkersHH= str(handle_status(twin_state_dictionary["LIT301Markers"]['HH']))
        DT_AIT301MarkersL = str(handle_status(twin_state_dictionary["AIT301Markers"]['L']))
        DT_AIT301MarkersH = str(handle_status(twin_state_dictionary["AIT301Markers"]['H']))
        DT_AIT302MarkersL = str(handle_status(twin_state_dictionary["AIT302Markers"]['L']))
        DT_AIT302MarkersH = str(handle_status(twin_state_dictionary["AIT302Markers"]['H']))
        DT_AIT303MarkersL = str(handle_status(twin_state_dictionary["AIT303Markers"]['L']))
        DT_AIT303MarkersH = str(handle_status(twin_state_dictionary["AIT303Markers"]['H']))
        DT_DPIT301MarkersL= str(handle_status(twin_state_dictionary["DPIT301Markers"]['L']))
        DT_DPIT301MarkersH= str(handle_status(twin_state_dictionary["DPIT301Markers"]['H']))


        DT_TimeStamp = str(handle_status(twin_state_dictionary["TimeStamp"])).replace(" ", "_")
        DT_SimStep = str(handle_status(twin_state_dictionary["SimStep"]))
        DT_SimSpeed = str(handle_status(twin_state_dictionary["Simspeed"]))
        DT_HealthCheckFrequency = str(handle_status(twin_state_dictionary["HCF"]))

        DT_AIT401 = str(handle_status(twin_state_dictionary["AIT401"]))
        DT_AIT402 = str(handle_status(twin_state_dictionary["AIT402"]))
        DT_FIT401 = str(handle_status(twin_state_dictionary["FIT401"]))
        DT_LIT401 = str(handle_status(twin_state_dictionary["LIT401"]))
        DT_LIT402 = str(handle_status(twin_state_dictionary["LIT402"]))
        DT_P401 = str(handle_status(twin_state_dictionary["P401"]))
        DT_P402 = str(handle_status(twin_state_dictionary["P402"]))
        DT_P403 = str(handle_status(twin_state_dictionary["P403"]))
        DT_P404 = str(handle_status(twin_state_dictionary["P404"]))
        DT_AIT501 = str(handle_status(twin_state_dictionary["AIT501"]))
        DT_AIT502 = str(handle_status(twin_state_dictionary["AIT502"]))
        DT_AIT503 = str(handle_status(twin_state_dictionary["AIT503"]))
        DT_AIT504 = str(handle_status(twin_state_dictionary["AIT504"]))
        DT_FIT501 = str(handle_status(twin_state_dictionary["FIT501"]))
        DT_FIT502 = str(handle_status(twin_state_dictionary["FIT502"]))
        DT_FIT503 = str(handle_status(twin_state_dictionary["FIT503"]))
        DT_FIT504 = str(handle_status(twin_state_dictionary["FIT504"]))
        DT_MV501 = str(handle_status(twin_state_dictionary["MV501"]))
        DT_MV502 = str(handle_status(twin_state_dictionary["MV502"]))
        DT_MV503 = str(handle_status(twin_state_dictionary["MV503"]))
        DT_MV504 = str(handle_status(twin_state_dictionary["MV504"]))
        DT_P501 = str(handle_status(twin_state_dictionary["P501"]))
        DT_P502 = str(handle_status(twin_state_dictionary["P502"]))
        DT_FIT601 = str(handle_status(twin_state_dictionary["FIT601"]))
        DT_P601 = str(handle_status(twin_state_dictionary["P601"]))
        DT_P602 = str(handle_status(twin_state_dictionary["P602"]))
        DT_LIT601 = str(handle_status(twin_state_dictionary["LIT601"]))
        DT_LIT602 = str(handle_status(twin_state_dictionary["LIT602"]))
        DT_PIT501 = str(handle_status(twin_state_dictionary["PIT501"]))
        DT_PIT502 = str(handle_status(twin_state_dictionary["PIT502"]))
        DT_PIT503 = str(handle_status(twin_state_dictionary["PIT503"]))
        DT_UV401 = str(handle_status(twin_state_dictionary["UV401"]))

        DT_FIT401MarkersL = str(handle_status(twin_state_dictionary["FIT401Markers"]['L']))
        DT_FIT401MarkersH = str(handle_status(twin_state_dictionary["FIT401Markers"]['H']))
        DT_AIT401MarkersL = str(handle_status(twin_state_dictionary["AIT401Markers"]['L']))
        DT_AIT401MarkersH = str(handle_status(twin_state_dictionary["AIT401Markers"]['H']))
        DT_AIT402MarkersL = str(handle_status(twin_state_dictionary["AIT402Markers"]['L']))
        DT_AIT402MarkersH = str(handle_status(twin_state_dictionary["AIT402Markers"]['H']))
        DT_LIT401MarkersLL= str(handle_status(twin_state_dictionary["LIT401Markers"]['LL']))
        DT_LIT401MarkersL = str(handle_status(twin_state_dictionary["LIT401Markers"]['L']))
        DT_LIT401MarkersH = str(handle_status(twin_state_dictionary["LIT401Markers"]['H']))
        DT_LIT401MarkersHH= str(handle_status(twin_state_dictionary["LIT401Markers"]['HH']))
        DT_LIT402MarkersLL= str(handle_status(twin_state_dictionary["LIT402Markers"]['LL']))
        DT_LIT402MarkersL = str(handle_status(twin_state_dictionary["LIT402Markers"]['L']))
        DT_LIT402MarkersH = str(handle_status(twin_state_dictionary["LIT402Markers"]['H']))
        DT_LIT402MarkersHH= str(handle_status(twin_state_dictionary["LIT402Markers"]['HH']))
        DT_PIT501MarkersL = str(handle_status(twin_state_dictionary["PIT501Markers"]['L']))
        DT_PIT501MarkersH = str(handle_status(twin_state_dictionary["PIT501Markers"]['H']))
        DT_PIT502MarkersL = str(handle_status(twin_state_dictionary["PIT502Markers"]['L']))
        DT_PIT502MarkersH = str(handle_status(twin_state_dictionary["PIT502Markers"]['H']))
        DT_PIT503MarkersL = str(handle_status(twin_state_dictionary["PIT503Markers"]['L']))
        DT_PIT503MarkersH = str(handle_status(twin_state_dictionary["PIT503Markers"]['H']))
        DT_FIT501MarkersL = str(handle_status(twin_state_dictionary["FIT501Markers"]['L']))
        DT_FIT501MarkersH = str(handle_status(twin_state_dictionary["FIT501Markers"]['H']))
        DT_FIT502MarkersL = str(handle_status(twin_state_dictionary["FIT502Markers"]['L']))
        DT_FIT502MarkersH = str(handle_status(twin_state_dictionary["FIT502Markers"]['H']))
        DT_FIT503MarkersL = str(handle_status(twin_state_dictionary["FIT503Markers"]['L']))
        DT_FIT503MarkersH = str(handle_status(twin_state_dictionary["FIT503Markers"]['H']))
        DT_FIT504MarkersL = str(handle_status(twin_state_dictionary["FIT504Markers"]['L']))
        DT_FIT504MarkersH = str(handle_status(twin_state_dictionary["FIT504Markers"]['H']))
        DT_AIT501MarkersL = str(handle_status(twin_state_dictionary["AIT501Markers"]['L']))
        DT_AIT501MarkersH = str(handle_status(twin_state_dictionary["AIT501Markers"]['H']))
        DT_AIT502MarkersL = str(handle_status(twin_state_dictionary["AIT502Markers"]['L']))
        DT_AIT502MarkersH = str(handle_status(twin_state_dictionary["AIT502Markers"]['H']))
        DT_AIT503MarkersL = str(handle_status(twin_state_dictionary["AIT503Markers"]['L']))
        DT_AIT503MarkersH = str(handle_status(twin_state_dictionary["AIT503Markers"]['H']))
        DT_AIT504MarkersL = str(handle_status(twin_state_dictionary["AIT504Markers"]['L']))
        DT_AIT504MarkersH = str(handle_status(twin_state_dictionary["AIT504Markers"]['H']))
        DT_FIT601MarkersL = str(handle_status(twin_state_dictionary["FIT601Markers"]['L']))
        DT_FIT601MarkersH = str(handle_status(twin_state_dictionary["FIT601Markers"]['H']))
        DT_LIT601MarkersLL=str(handle_status(twin_state_dictionary["LIT601Markers"]['LL']))
        DT_LIT601MarkersL = str(handle_status(twin_state_dictionary["LIT601Markers"]['L']))
        DT_LIT601MarkersH = str(handle_status(twin_state_dictionary["LIT601Markers"]['H']))
        DT_LIT601MarkersHH=str(handle_status(twin_state_dictionary["LIT601Markers"]['HH']))
        DT_LIT602MarkersLL=str(handle_status(twin_state_dictionary["LIT602Markers"]['LL']))
        DT_LIT602MarkersL = str(handle_status(twin_state_dictionary["LIT602Markers"]['L']))
        DT_LIT602MarkersH = str(handle_status(twin_state_dictionary["LIT602Markers"]['H']))
        DT_LIT602MarkersHH=str(handle_status(twin_state_dictionary["LIT602Markers"]['HH']))

        DT_P101Mode = str(handle_status(twin_state_dictionary["P101Mode"]))
        DT_P102Mode = str(handle_status(twin_state_dictionary["P102Mode"]))
        DT_P201Mode = str(handle_status(twin_state_dictionary["P201Mode"]))
        DT_P202Mode = str(handle_status(twin_state_dictionary["P202Mode"]))
        DT_P203Mode = str(handle_status(twin_state_dictionary["P203Mode"]))
        DT_P204Mode = str(handle_status(twin_state_dictionary["P204Mode"]))
        DT_P205Mode = str(handle_status(twin_state_dictionary["P205Mode"]))
        DT_P206Mode = str(handle_status(twin_state_dictionary["P206Mode"]))
        DT_P301Mode = str(handle_status(twin_state_dictionary["P301Mode"]))
        DT_P302Mode = str(handle_status(twin_state_dictionary["P302Mode"]))
        DT_MV101Mode = str(handle_status(twin_state_dictionary["MV101Mode"]))
        DT_MV201Mode = str(handle_status(twin_state_dictionary["MV201Mode"]))
        DT_MV302Mode = str(handle_status(twin_state_dictionary["MV302Mode"]))
        
        DT_P401Mode = str(handle_status(twin_state_dictionary["P401Mode"]))
        DT_P402Mode = str(handle_status(twin_state_dictionary["P402Mode"]))
        DT_P403Mode = str(handle_status(twin_state_dictionary["P403Mode"]))
        DT_P404Mode = str(handle_status(twin_state_dictionary["P404Mode"]))
        DT_P501Mode = str(handle_status(twin_state_dictionary["P501Mode"]))
        DT_P502Mode = str(handle_status(twin_state_dictionary["P502Mode"]))
        DT_P601Mode = str(handle_status(twin_state_dictionary["P601Mode"]))
        DT_P602Mode = str(handle_status(twin_state_dictionary["P602Mode"]))
        DT_MV501Mode = str(handle_status(twin_state_dictionary["MV501Mode"]))
        DT_MV502Mode = str(handle_status(twin_state_dictionary["MV502Mode"]))
        DT_MV503Mode = str(handle_status(twin_state_dictionary["MV503Mode"]))
        DT_MV504Mode = str(handle_status(twin_state_dictionary["MV504Mode"]))

        DT_P207Mode = "0" #str(handle_status(twin_state_dictionary["P207Mode"]))
        DT_P208Mode = "0" #str(handle_status(twin_state_dictionary["P208Mode"]))
        DT_MV301Mode = str(handle_status(twin_state_dictionary["MV301Mode"]))
        DT_MV303Mode = str(handle_status(twin_state_dictionary["MV303Mode"]))
        DT_MV304Mode = str(handle_status(twin_state_dictionary["MV304Mode"]))
        DT_UV401Mode = str(handle_status(twin_state_dictionary["UV401Mode"]))

        DT_PI101   = str(handle_status(twin_state_dictionary["PI101"]))
        DT_PI102   = str(handle_status(twin_state_dictionary["PI102"]))
        DT_PI301   = str(handle_status(twin_state_dictionary["PI301"]))
        DT_PI302   = str(handle_status(twin_state_dictionary["PI302"]))
        DT_PI303   = str(handle_status(twin_state_dictionary["PI303"]))
        DT_PI304   = str(handle_status(twin_state_dictionary["PI304"]))
        DT_PI305   = str(handle_status(twin_state_dictionary["PI305"]))
        DT_PI401   = str(handle_status(twin_state_dictionary["PI401"]))
        DT_PI402   = str(handle_status(twin_state_dictionary["PI402"]))
        DT_PI501   = str(handle_status(twin_state_dictionary["PI501"]))
        DT_PI502   = str(handle_status(twin_state_dictionary["PI502"]))
        DT_PI601   = str(handle_status(twin_state_dictionary["PI601"]))
        DT_PI602   = str(handle_status(twin_state_dictionary["PI602"]))
        DT_DPSH301 = str(handle_status(twin_state_dictionary["DPSH301"]))
        DT_PSH301  = str(handle_status(twin_state_dictionary["PSH301"]))
        DT_PSL501  = str(handle_status(twin_state_dictionary["PSL501"]))
        DT_PSH501  = str(handle_status(twin_state_dictionary["PSH501"]))

        DT_LS201 = str(handle_status(twin_state_dictionary["LS201"]))
        DT_LS202 = str(handle_status(twin_state_dictionary["LS202"]))
        DT_LSL203 = str(handle_status(twin_state_dictionary["LSL203"]))
        DT_LSLL203 = str(handle_status(twin_state_dictionary["LSLL203"]))
        DT_LS401 = str(handle_status(twin_state_dictionary["LS401"]))
        DT_LSL601 = str(handle_status(twin_state_dictionary["LSL601"]))
        DT_LSH601 = str(handle_status(twin_state_dictionary["LSH601"]))
        DT_LSL602 = str(handle_status(twin_state_dictionary["LSL602"]))
        DT_LSH602 = str(handle_status(twin_state_dictionary["LSH602"]))

        DT_LIT101Mode = str(handle_status(twin_state_dictionary["LIT101Mode"]))
        DT_FIT101Mode = str(handle_status(twin_state_dictionary["FIT101Mode"]))
        DT_LIT201Mode = str(handle_status(twin_state_dictionary["LIT201Mode"]))
        DT_LIT202Mode = str(handle_status(twin_state_dictionary["LIT202Mode"]))
        DT_LIT203Mode = str(handle_status(twin_state_dictionary["LIT203Mode"]))
        DT_AIT201Mode = str(handle_status(twin_state_dictionary["AIT201Mode"]))
        DT_AIT201Mode = str(handle_status(twin_state_dictionary["AIT201Mode"]))
        DT_AIT202Mode = str(handle_status(twin_state_dictionary["AIT202Mode"]))
        DT_AIT203Mode = str(handle_status(twin_state_dictionary["AIT203Mode"]))
        DT_FIT201Mode = str(handle_status(twin_state_dictionary["FIT201Mode"]))
        DT_LIT301Mode = str(handle_status(twin_state_dictionary["LIT301Mode"]))
        DT_DPIT301Mode= str(handle_status(twin_state_dictionary["DPIT301Mode"]))
        DT_FIT301Mode = str(handle_status(twin_state_dictionary["FIT301Mode"]))
        DT_AIT301Mode = str(handle_status(twin_state_dictionary["AIT301Mode"]))
        DT_AIT302Mode = str(handle_status(twin_state_dictionary["AIT302Mode"]))
        DT_AIT303Mode = str(handle_status(twin_state_dictionary["AIT303Mode"]))
        DT_LIT401Mode = str(handle_status(twin_state_dictionary["LIT401Mode"]))
        DT_LIT402Mode = str(handle_status(twin_state_dictionary["LIT402Mode"]))
        DT_FIT401Mode = str(handle_status(twin_state_dictionary["FIT401Mode"]))
        DT_AIT401Mode = str(handle_status(twin_state_dictionary["AIT401Mode"]))
        DT_AIT402Mode = str(handle_status(twin_state_dictionary["AIT402Mode"]))
        DT_AIT501Mode = str(handle_status(twin_state_dictionary["AIT501Mode"]))
        DT_AIT502Mode = str(handle_status(twin_state_dictionary["AIT502Mode"]))
        DT_AIT503Mode = str(handle_status(twin_state_dictionary["AIT503Mode"]))
        DT_AIT504Mode = str(handle_status(twin_state_dictionary["AIT504Mode"]))
        DT_FIT501Mode = str(handle_status(twin_state_dictionary["FIT501Mode"]))
        DT_FIT502Mode = str(handle_status(twin_state_dictionary["FIT502Mode"]))
        DT_FIT503Mode = str(handle_status(twin_state_dictionary["FIT503Mode"]))
        DT_FIT504Mode = str(handle_status(twin_state_dictionary["FIT504Mode"]))
        DT_PIT501Mode = str(handle_status(twin_state_dictionary["PIT501Mode"]))
        DT_PIT502Mode = str(handle_status(twin_state_dictionary["PIT502Mode"]))
        DT_PIT503Mode = str(handle_status(twin_state_dictionary["PIT503Mode"]))
        DT_LIT601Mode = str(handle_status(twin_state_dictionary["LIT601Mode"]))
        DT_LIT602Mode = str(handle_status(twin_state_dictionary["LIT602Mode"]))
        DT_FIT601Mode = str(handle_status(twin_state_dictionary["FIT601Mode"]))

        DT_DADAnomaly = str(handle_status(twin_state_dictionary["DAD"]['Anomaly']))
        DT_DADInvariants = str(handle_status(twin_state_dictionary["DAD"]['Invariants'])).replace(" ", "=") # remove spaces as it will split the messages
        DT_AICritAnomaly = str(handle_status(twin_state_dictionary["AICrit"]['Anomaly']))
        DT_AICritInvariants = str(handle_status(twin_state_dictionary["AICrit"]['Invariants'])).replace(" ", "=")

        try:
            DT_RWmsg = str(twin_state_dictionary["RWmsg"]).replace(" ", "_")
        except:
            DT_RWmsg = 'None'
        try:
            DT_CDmsg = str(twin_state_dictionary["CDmsg"]).replace(" ", "_")
        except:
            DT_CDmsg = 'None'
        try:
            DT_UFmsg = str(twin_state_dictionary["UFmsg"]).replace(" ", "_")
        except:
            DT_UFmsg = 'None'
        try:
            DT_UVmsg = str(twin_state_dictionary["UVmsg"]).replace(" ", "_")
        except:
            DT_UVmsg = 'None'
        try:
            DT_ROmsg = str(twin_state_dictionary["ROmsg"]).replace(" ", "_")
        except:
            DT_ROmsg = 'None'
        try:
            DT_BWmsg = str(twin_state_dictionary["BWmsg"]).replace(" ", "_")
        except:
            DT_BWmsg = 'None'

        try:
            DT_PLC1 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC1"])) 
        except:
            DT_PLC1 = 'False'
        try:
            DT_PLC2 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC2"])) 
        except:
            DT_PLC2 = "False"
        try:
            DT_PLC3 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC3"])) 
        except:
            DT_PLC3 = "False"
        try:
            DT_PLC4 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC4"])) 
        except:
            DT_PLC4 = "False"
        try:
            DT_PLC5 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC5"])) 
        except:
            DT_PLC5 = "False"
        try:
            DT_PLC6 = str(handle_status(twin_state_dictionary["PLCStatus"]["PLC6"])) 
        except:
            DT_PLC6 = "False"

        try:
            DT_ButtonRunStop = str(handle_status(twin_state_dictionary["HMIButtonState"]["RunStop"])) 
        except:
            DT_ButtonRunStop = 'Disable'
        try:
            DT_ButtonStep = str(handle_status(twin_state_dictionary["HMIButtonState"]["Step"])) 
        except:
            DT_ButtonStep = 'Disable'
        try:
            DT_ButtonEmptyTanks = str(handle_status(twin_state_dictionary["HMIButtonState"]["EmptyTanks"])) 
        except:
            DT_ButtonEmptyTanks = 'Disable'
        try:
            DT_ButtonSpeed = str(handle_status(twin_state_dictionary["HMIButtonState"]["Speed"])) 
        except:
            DT_ButtonSpeed = 'Disable'

        DT_WaterAvailableStage6 = str(handle_status(twin_state_dictionary["waterAvailableStage6"]))
        DT_HMI_Title = "Klein_Water_Technologies_Treatment_Plant"


        PythonUnityBridge_message = DT_Terminate + " " + DT_FIT101 + " " + \
            DT_LIT101 + " " + \
            DT_MV101 + " " + \
            DT_P101 +  " " + \
            DT_P102 +  " " + \
            DT_AIT201 +  " " + \
            DT_AIT202 +  " " + \
            DT_AIT203 +  " " + \
            DT_FIT201 +  " " + \
            DT_MV201 +  " " + \
            DT_P201 +  " " + \
            DT_P202 +  " " + \
            DT_P203 +  " " + \
            DT_P204 +  " " + \
            DT_P205 +  " " + \
            DT_P206 +  " " + \
            DT_AIT301 +  " " + \
            DT_AIT302 +  " " + \
            DT_AIT303 +  " " + \
            DT_DPIT301 +  " " + \
            DT_FIT301 +  " " + \
            DT_LIT301 +  " " + \
            DT_MV301 +  " " + \
            DT_MV302 +  " " + \
            DT_MV303 +  " " + \
            DT_MV304 +  " " + \
            DT_P301 +  " " + \
            DT_P302 +  " " + \
            DT_HMI_Mode + " " + \
            DT_Control + " " + \
            DT_TimeStamp + " " + \
            DT_LIT201 +  " " + \
            DT_LIT202 +  " " + \
            DT_LIT203 +  " " + \
            DT_FIT101MarkersL + " " + \
            DT_FIT101MarkersH + " " + \
            DT_FIT201MarkersL + " " + \
            DT_FIT201MarkersH + " " + \
            DT_LIT101MarkersLL + " " + \
            DT_LIT101MarkersL + " " + \
            DT_LIT101MarkersH + " " + \
            DT_LIT101MarkersHH + " " + \
            DT_LIT201MarkersLL + " " + \
            DT_LIT201MarkersL + " " + \
            DT_LIT201MarkersH + " " + \
            DT_LIT201MarkersHH + " " + \
            DT_LIT202MarkersLL + " " + \
            DT_LIT202MarkersL + " " + \
            DT_LIT202MarkersH + " " + \
            DT_LIT202MarkersHH + " " + \
            DT_LIT203MarkersLL + " " + \
            DT_LIT203MarkersL + " " + \
            DT_LIT203MarkersH + " " + \
            DT_LIT203MarkersHH + " " + \
            DT_AIT201MarkersL + " " + \
            DT_AIT201MarkersH + " " + \
            DT_AIT202MarkersL + " " + \
            DT_AIT202MarkersH + " " + \
            DT_AIT203MarkersL + " " + \
            DT_AIT203MarkersH + " " + \
            DT_FIT301MarkersL + " " + \
            DT_FIT301MarkersH + " " + \
            DT_LIT301MarkersLL + " " + \
            DT_LIT301MarkersL + " " + \
            DT_LIT301MarkersH + " " + \
            DT_LIT301MarkersHH + " " + \
            DT_AIT301MarkersL + " " + \
            DT_AIT301MarkersH + " " + \
            DT_AIT302MarkersL + " " + \
            DT_AIT302MarkersH + " " + \
            DT_AIT303MarkersL + " " + \
            DT_AIT303MarkersH + " " + \
            DT_DPIT301MarkersL + " " + \
            DT_DPIT301MarkersH + " " + \
            DT_HMI_In_Control + " " + \
            DT_SimStep + " " + \
            DT_HMI_Mode2 + " " + \
            DT_HMI_Mode3 + " " + \
            DT_HMI_Mode4 + " " + \
            DT_HMI_Mode5 + " " + \
            DT_SimSpeed + " " + \
            DT_HealthCheckFrequency + " " + \
            DT_AIT401 +  " " + \
            DT_AIT402 +  " " + \
            DT_FIT401 +  " " + \
            DT_LIT401 +  " " + \
            DT_LIT402 +  " " + \
            DT_P401 +  " " + \
            DT_P402 +  " " + \
            DT_P403 +  " " + \
            DT_P404 +  " " + \
            DT_AIT501 +  " " + \
            DT_AIT502 +  " " + \
            DT_AIT503 +  " " + \
            DT_AIT504 +  " " + \
            DT_FIT501 +  " " + \
            DT_FIT502 +  " " + \
            DT_FIT503 +  " " + \
            DT_FIT504 +  " " + \
            DT_MV501 +  " " + \
            DT_MV502 +  " " + \
            DT_MV503 +  " " + \
            DT_MV504 +  " " + \
            DT_P501 +  " " + \
            DT_P502 +  " " + \
            DT_P601 +  " " + \
            DT_P602 +  " " + \
            DT_LIT601 +  " " + \
            DT_LIT602 +  " " + \
            DT_PIT501 +  " " + \
            DT_PIT502 +  " " + \
            DT_PIT503 +  " " + \
            DT_UV401 + " " + \
            DT_FIT601 +  " " + \
            DT_FIT401MarkersL  + " " + \
            DT_FIT401MarkersH  + " " + \
            DT_AIT401MarkersL  + " " + \
            DT_AIT401MarkersH  + " " + \
            DT_AIT402MarkersL  + " " + \
            DT_AIT402MarkersH  + " " + \
            DT_LIT401MarkersLL + " " + \
            DT_LIT401MarkersL  + " " + \
            DT_LIT401MarkersH  + " " + \
            DT_LIT401MarkersHH + " " + \
            DT_LIT402MarkersLL + " " + \
            DT_LIT402MarkersL  + " " + \
            DT_LIT402MarkersH  + " " + \
            DT_LIT402MarkersHH + " " + \
            DT_PIT501MarkersL  + " " + \
            DT_PIT501MarkersH  + " " + \
            DT_PIT502MarkersL  + " " + \
            DT_PIT502MarkersH  + " " + \
            DT_PIT503MarkersL  + " " + \
            DT_PIT503MarkersH  + " " + \
            DT_FIT501MarkersL  + " " + \
            DT_FIT501MarkersH  + " " + \
            DT_FIT502MarkersL  + " " + \
            DT_FIT502MarkersH  + " " + \
            DT_FIT503MarkersL  + " " + \
            DT_FIT503MarkersH  + " " + \
            DT_FIT504MarkersL  + " " + \
            DT_FIT504MarkersH  + " " + \
            DT_AIT501MarkersL  + " " + \
            DT_AIT501MarkersH  + " " + \
            DT_AIT502MarkersL  + " " + \
            DT_AIT502MarkersH  + " " + \
            DT_AIT503MarkersL  + " " + \
            DT_AIT503MarkersH  + " " + \
            DT_AIT504MarkersL  + " " + \
            DT_AIT504MarkersH  + " " + \
            DT_FIT601MarkersL  + " " + \
            DT_FIT601MarkersH  + " " + \
            DT_LIT601MarkersLL + " " + \
            DT_LIT601MarkersL  + " " + \
            DT_LIT601MarkersH  + " " + \
            DT_LIT601MarkersHH + " " + \
            DT_LIT602MarkersLL + " " + \
            DT_LIT602MarkersL  + " " + \
            DT_LIT602MarkersH  + " " + \
            DT_LIT602MarkersHH + " " + \
            DT_P101Mode + " " + \
            DT_P102Mode + " " + \
            DT_P201Mode + " " + \
            DT_P202Mode + " " + \
            DT_P203Mode + " " + \
            DT_P204Mode + " " + \
            DT_P205Mode + " " + \
            DT_P206Mode + " " + \
            DT_P301Mode + " " + \
            DT_P302Mode + " " + \
            DT_MV101Mode + " " + \
            DT_MV201Mode + " " + \
            DT_MV302Mode + " " + \
            DT_P401Mode + " " + \
            DT_P402Mode + " " + \
            DT_P403Mode + " " + \
            DT_P404Mode + " " + \
            DT_P501Mode + " " + \
            DT_P502Mode + " " + \
            DT_P601Mode + " " + \
            DT_P602Mode + " " + \
            DT_MV501Mode + " " + \
            DT_MV502Mode + " " + \
            DT_MV503Mode + " " + \
            DT_MV504Mode + " " + \
            DT_P207Mode + " " + \
            DT_P208Mode + " " + \
            DT_MV301Mode + " " + \
            DT_MV303Mode + " " + \
            DT_MV304Mode + " " + \
            DT_UV401Mode + " " + \
            DT_PI101 + " " + \
            DT_PI102 + " " + \
            DT_PI301 + " " + \
            DT_PI302 + " " + \
            DT_PI303 + " " + \
            DT_PI304 + " " + \
            DT_PI305 + " " + \
            DT_PI401 + " " + \
            DT_PI402 + " " + \
            DT_PI501 + " " + \
            DT_PI502 + " " + \
            DT_PI601 + " " + \
            DT_PI602 + " " + \
            DT_DPSH301 + " " + \
            DT_PSH301 + " " + \
            DT_PSL501 + " " + \
            DT_PSH501 + " " + \
            DT_LS201 + " " + \
            DT_LS202 + " " + \
            DT_LSL203 + " " + \
            DT_LSLL203 + " " + \
            DT_LS401 + " " + \
            DT_LSL601 + " " + \
            DT_LSH601 + " " + \
            DT_LSL602 + " " + \
            DT_LSH602 + " " + \
            DT_Exercise + " " + \
            DT_Version + " " + \
            DT_LIT101Mode + " " + \
            DT_FIT101Mode + " " + \
            DT_LIT201Mode + " " + \
            DT_LIT202Mode + " " + \
            DT_LIT203Mode + " " + \
            DT_AIT201Mode + " " + \
            DT_AIT202Mode + " " + \
            DT_AIT203Mode + " " + \
            DT_FIT201Mode + " " + \
            DT_LIT301Mode + " " + \
            DT_DPIT301Mode + " " + \
            DT_FIT301Mode + " " + \
            DT_AIT301Mode + " " + \
            DT_AIT302Mode + " " + \
            DT_AIT303Mode + " " + \
            DT_LIT401Mode + " " + \
            DT_LIT402Mode + " " + \
            DT_FIT401Mode + " " + \
            DT_AIT401Mode + " " + \
            DT_AIT402Mode + " " + \
            DT_AIT501Mode + " " + \
            DT_AIT502Mode + " " + \
            DT_AIT503Mode + " " + \
            DT_AIT504Mode + " " + \
            DT_FIT501Mode + " " + \
            DT_FIT502Mode + " " + \
            DT_FIT503Mode + " " + \
            DT_FIT504Mode + " " + \
            DT_PIT501Mode + " " + \
            DT_PIT502Mode + " " + \
            DT_PIT503Mode + " " + \
            DT_LIT601Mode + " " + \
            DT_LIT602Mode + " " + \
            DT_FIT601Mode + " " + \
            DT_DADAnomaly + " " + \
            DT_DADInvariants + " " + \
            DT_AICritAnomaly + " " + \
            DT_AICritInvariants + " " + \
            DT_RWmsg + " " + \
            DT_CDmsg + " " + \
            DT_UFmsg + " " + \
            DT_UVmsg + " " + \
            DT_ROmsg + " " + \
            DT_BWmsg + " " + \
            DT_PLC1 + " " + \
            DT_PLC2 + " " + \
            DT_PLC3 + " " + \
            DT_PLC4 + " " + \
            DT_PLC5 + " " + \
            DT_PLC6 + " " + \
            DT_ButtonRunStop + " " + \
            DT_ButtonStep + " " + \
            DT_ButtonEmptyTanks + " " + \
            DT_ButtonSpeed + " " + \
            DT_WaterAvailableStage6 + " " + \
            DT_HMI_Title

        #print(DT_SimStep)
        return PythonUnityBridge_message

def main():
    hmi_pub_topic = ""
    empty_hmi_object = "{\"ID\": 1, \"Request\": \"None\", \"Emergency\": \"None\", \"Control\": None, \"HMI\": None}";    
    empty_hmi_message = "{0} {1}".format(hmi_pub_topic, empty_hmi_object)
    HMIObject = empty_hmi_object
    twin_objects_received = 0
    unity_objects_received = 0
    total_value = 0
    steps = 1
    AVERAGE_TIMER = 1 * 60 * 60

    watch = time.perf_counter
    hourly_timer = watch()
    measure = {}
    elapsed = {}
    # twin_message = "Extract! {'Version': 'Aug 26, 2021 Version CISS2021_03.06. ZMQ.OPC', 'LS201': 0, 'LS202': 0, 'LSL203': 0,'LSLL203': 0, 'LS401': 0,'LSL601': 0, 'LSH601': 0,'LSL602': 0,'LSH602': 0,'PI101': 38, 'PI102': 36, 'PI301': 22, 'PI302': 23, 'PI303': 10, 'PI304': 10, 'PI305': 6, 'PI401': 44, 'PI402': 44, 'PI501': 36, 'PI502': 36, 'PI601': 20, 'PI602': 46, 'DPSH301': 12,'PSH301': 23,'PSL501': 11,'PSH501': 22, 'UV401Mode': '*', 'P207Mode': '*', 'P208Mode': '*', 'MV301Mode': '*', 'MV303Mode': '*', 'MV304Mode': '*', 'P101Mode': '*','P102Mode': '*','P201Mode': '*','P202Mode': '*','P203Mode': '*','P204Mode': '*','P205Mode': '*','P206Mode': '*','P301Mode': '*','P302Mode': '*','P401Mode': '*','P402Mode': '*','P403Mode': '*','P404Mode': '*','P501Mode': '*','P502Mode': '*','P601Mode': '*','P602Mode': '*','MV101Mode': '*','MV201Mode': '*','MV302Mode': '*','MV501Mode': '*','MV502Mode': '*','MV503Mode': '*','MV504Mode': '*', 'LIT402Markers': {'LL': 15, 'L': 30 , 'H': 200 ,'HH': 200 },'DPIT301Markers':{'L': 0.1, 'H': 0.4}, 'FIT401Markers': { 'L': 1.3, 'H': 2.0 }, 'LIT401Markers': {'LL': 250, 'L': 800 , 'H': 1000 ,'HH': 1200 }, 'AIT401Markers': {'L': 5, 'H': 200 }, 'AIT402Markers': {'L': 153.7811, 'H': 235.7088},'PIT501Markers': {'L': 160, 'H': 300},'PIT502Markers': {'L': 250, 'H': 320},'PIT503Markers': {'L': 140, 'H': 210},'FIT501Markers': {'L': 1.1, 'H': 1.6},'FIT502Markers': {'L': 0.7, 'H': 1.1}, 'FIT503Markers': {'L': 0.7, 'H': 0.9}, 'FIT504Markers': {'L': 0.2, 'H': 0.4}, 'AIT501Markers': {'L': 7.303769, 'H': 7.925084},'AIT502Markers': {'L': 142.3841, 'H': 218.3286},'AIT503Markers': {'L': 252.0828, 'H': 283.3568},'AIT504Markers': {'L': 7.344271, 'H': 309.1899},'FIT601OMarkers':{'L': 1.6, 'H': 1.8},'FIT601Markers': { 'L': 1.6, 'H': 1.8},'LIT601Markers': { 'LL': 200, 'L': 200, 'H': 700, 'HH': 800},'LIT602Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 700}, 'LIT402': 555, 'PLC1_STATE': 3, 'PLC2_STATE': 2, 'PLC3_STATE': 7, 'PLC4_STATE': 2, 'PLC5_STATE': 12, 'PLC6_STATE': 3, 'SimStep': 23296, 'PlantState': 'Running', 'Simspeed': 100, 'Exercise': 'CISS', 'WaterAvailable': 3, 'PLC1Reset': False, 'PLC2Reset': False, 'PLC3Reset': False, 'PLC4Reset': False, 'PLC5Reset': False, 'PLC6Reset': False, 'Tunnel': False, 'FIT101Markers': {'L': 2.5, 'H': 2.7}, 'FIT201Markers': {'L': 2.4, 'H': 2.6}, 'LIT101Markers': {'LL': 250, 'L': 500, 'H': 800, 'HH': 1200}, 'LIT201Markers': {'LL': 5, 'L': 5, 'H': 250, 'HH': 250}, 'LIT202Markers': {'LL': 2, 'L': 2, 'H': 25, 'HH': 25}, 'LIT203Markers': {'LL': 2, 'L': 5, 'H': 200, 'HH': 200}, 'AIT201Markers': {'L': 244.3284, 'H': 272.5263}, 'AIT202Markers': {'L': 6.95, 'H': 8.988273}, 'AIT203Markers': {'L': 300.8459, 'H': 567.46699}, 'FIT301Markers': {'L': 2.2, 'H': 2.4}, 'LIT301Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'AIT301Markers': {'L': 5.97, 'H': 6.2}, 'AIT302Markers': {'L': 393.7256, 'H': 395.774384}, 'AIT303Markers': {'L': 305.810211, 'H': 305.86783499999996}, 'DPIT301Markers': {'L': 0.1, 'H': 0.4}, 'DRN-TNKMarkers': {'LL': 0, 'L': 100, 'H': 800, 'HH': 1200}, 'FIT401Markers': {'L': 1.3, 'H': 2.0}, 'LIT401Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'AIT401Markers': {'L': 5, 'H': 200}, 'AIT402Markers': {'L': 153.7811, 'H': 235.7088}, 'PIT501Markers': {'L': 160, 'H': 300}, 'PIT502Markers': {'L': 250, 'H': 320}, 'PIT503Markers': {'L': 140, 'H': 210}, 'FIT501Markers': {'L': 1.1, 'H': 1.6}, 'FIT502Markers': {'L': 0.7, 'H': 1.1}, 'FIT503Markers': {'L': 0.7, 'H': 0.9}, 'FIT504Markers': {'L': 0.2, 'H': 0.4}, 'AIT501Markers': {'L': 7.303769, 'H': 7.925084}, 'AIT502Markers': {'L': 142.3841, 'H': 218.3286}, 'AIT503Markers': {'L': 252.0828, 'H': 283.3568}, 'AIT504Markers': {'L': 7.344271, 'H': 309.1899}, 'ROD-BWTMarkers': {'L': 1.6, 'H': 1.8}, 'FIT601OMarkers': {'L': 1.6, 'H': 1.8}, 'FIT601Markers': {'L': 1.6, 'H': 1.8}, 'LIT601Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 800}, 'LIT602Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 700}, 'MV101': 3, 'MV101T': 0, 'MV101ON': 6, 'MV101Reset': False, 'msgMV101': 'MV101 is closed', 'FIT101': 0.0778522822246754, 'LIT101': 733.6019373694243, 'T101Level': 733.3970048040612, 'WIN101': 12.757407339248086, 'WOUT101': 12.557883803401008, 'PlantInflow': 0.0, 'T101IFA': 0.00048185055555555553, 'T101OFA': 0.000686395, 'P101': 2, 'P101T': 0, 'FIT201': 2.571586210708107, 'FlowStatus': [False, True], 'P101ON': 17652, 'P101Reset': False, 'P102': 3, 'P102T': 0, 'P102ON': 0, 'P102Reset': False, 'RW': 2, 'PLC1_COMM': '', 'RWCount': 0, 'RWCycle': 3, 'RWCycleCount': 19732, 'RWmsg': 'MV201 is open; T101 and T301 levels OK; P101 is running.', 'RWCycleError': 'None', 'RWCycleDuration': [0, 0, 0, 0], 'PlantReset': False, 'MV201': 2, 'MV201T': 0, 'MV201ON': 133, 'MV201Reset': False, 'msgMV201': 'Steps to open: 0', 'P201': 1, 'P201T': 0, 'P201ON': 1420, 'P201Reset': False, 'msgP201': 'Dosing pump P201 stopped', 'P202': 3, 'P202T': 0, 'P202ON': 0, 'P202Reset': False, 'msgP202': '', 'P203': 2, 'P203T': 0, 'P203ON': 14020, 'P203Reset': False, 'msgP203': 'Dosing pump P203 running', 'P204': 3, 'P204T': 0, 'P204ON': 0, 'P204Reset': False, 'msgP204': '', 'P205': 2, 'P205T': 0, 'P205ON': 17475, 'P205Reset': False, 'msgP205': 'Dosing pump P205 running', 'P206': 3, 'P206T': 0, 'P206ON': 0, 'P206Reset': False, 'msgP206': '', 'LIT201': 127.22222275196957, 'T201Level': 126.77777777780548, 'msgT201': 'T201 initialized', 'LIT202': 18.859496330276457, 'T202Level': 18.50000000000005, 'msgT202': 'T202 below LL; reset to HH via auto tank fill', 'LIT203': 10.326418476569245, 'T203Level': 8.881944444453474, 'msgT203': 'T203 below LL; reset to HH via auto tank fill', 'CD': 3, 'CDCycle': 2, 'PLC2_COMM': '', 'AIT201': 264.8855226550347, 'AIT202': 9.845513982170784, 'AIT203': 570.4757407689013, 'CDCount': 0, 'CDCycleCount': 1700, 'CDmsg': 'MV201 open, AIT202 condition satisfied, run command to P203', 'CDCycleError': 'None', 'CDCycleDuration': [0, 0, 0, 0], 'ChemicalDosing': 3, 'FillCount201': 0, 'FillCount202': 5, 'FillCount203': 7, 'MV301': 3, 'MV301T': 0, 'MV301ON': 82, 'MV301Reset': False, 'msgMV301': 'MV301 is closed', 'MV302': 2, 'MV302T': 0, 'MV302ON': 83, 'MV302Reset': False, 'MV303': 3, 'MV303T': 0, 'MV303ON': 82, 'MV303Reset': False, 'MV304': 3, 'MV304T': 0, 'MV304ON': 84, 'MV304Reset': False, 'LIT301': 995.2316011400399, 'T301Level': 994.6105114220655, 'WIN301': 12.102508596221378, 'WOUT301': 11.808435843371582, 'T301Inflow': 0.000686395, 'T301IFA': 0.000686395, 'T301OFA': 0.0006192727777777779, 'P301': 2, 'P301T': 0, 'P301ON': 18887, 'FIT301': 2.316238155170955, 'DPIT301': 20.29526701705907, 'AIT301': 6.317475684216976, 'AIT302': 394.0980842838801, 'AIT303': 306.0563488588183, 'P301Reset': False, 'P302': 3, 'P302T': 0, 'P302ON': 0, 'P302Reset': False, 'RIOUFCount': 105, 'PLC3_COMM': '', 'UF': 6, 'UFCycle': 'Filtration', 'UFCount': 107, 'UFCycleCount': 42, 'Ultrafiltration': 3, 'UFTimer': 493, 'UFCycleError': 'None', 'BC': 41, 'p201Start': False, 'FIT301ToT401': 2.229382, 'LIT401': 969.3898181553216, 'T401Level': 969.0635907064674, 'WIN401': 10.574959346057268, 'WOUT401': 10.32059314805976, 'T401Inflow': 0.0006192727777777779, 'T401IFA': 0.0006192727777777779, 'T401OFA': 0.0004549611111111111, 'P401': 2, 'P401T': 0, 'P401ON': 23195, 'FIT401': 1.6853054491347779, 'FIT501P401': 1.153294, 'P401Reset': False, 'P402': 3, 'P402T': 0, 'P402ON': 0, 'msgP402': '. Command received at P401: Stop', 'FIT501P402': 0, 'P402Reset': False, 'P403': 3, 'P403T': 0, 'P403ON': 0, 'AIT401': 148.8022, 'msgP403': ' Steps to stop: 0.  Steps To No Flow0', 'P403Reset': False, 'P404': 3, 'P404T': 0, 'P404ON': 0, 'AIT402': 154.71257058180421, 'P404Reset': False, 'UV401': 2, 'AIT501': 8.551861098782878, 'AIT502': 218.73563066932294, 'AIT503': 274.0255001627823, 'AIT504': 314.99310071511013, 'UV401T': 0, 'UV401ON': 23193, 'UV401Reset': False, 'PLC4_COMM': 'WP: SCADA. ', 'UV': 3, 'UVState': 2, 'UVCount': 23196, 'UVCycle': 'ON UV', 'UVTimer': 0, 'UVCycleCount': 0, 'UVCycleError': 'None', 'Dechlorination': 3, 'MV501': 2, 'MV501T': 0, 'MV501ON': 3, 'MV501Reset': False, 'msgMV501': 'Steps to open: 0', 'MV502': 2, 'MV502T': 0, 'MV502ON': 3, 'MV502Reset': False, 'msgMV502': 'Steps to open: 0', 'MV503': 3, 'MV503T': 0, 'MV503ON': 2, 'MV503Reset': False, 'msgMV503': 'MV503 is closed', 'MV504': 3, 'MV504T': 0, 'MV504ON': 2, 'MV504Reset': False, 'P501': 2, 'P501Start': 3, 'P501T': 0, 'P501ON': 22891, 'FIT501': 1.6542636041518008, 'FIT502': 1.0893280396045741, 'FIT503': 0.6266271138335255, 'FIT504': 0.3994336160755564, 'PIT501': 264.7264322271708, 'PIT502': 3.1080006835665617, 'PIT503': 200.786409617422, 'AIT501High': False, 'P501Reset': False, 'P502': 3, 'P502T': 0, 'P502ON': 0, 'msgP502': '', 'RODrain': 0, 'RIOROCount': 22842, 'PLC5_COMM': 'WP: SCADA. ', 'RO': 11, 'ROCycle': 'PRM', 'ROCount': 22844, 'ROCycleCount': 3, 'ROTimer': 0, 'ROCycleError': 'None', 'StopP401': False, 'RunP401': False, 'FLV': False, 'ReverseOsmosis': 3, 'ShutdownCount': 0, 'LIT601': 700.4509209180582, 'T601Level': 700, 'WIN601': 6.774683282391907, 'WOUT601': 10.84916577400912, 'msg601': 'T601 draining: 4761.872583261988', 'T601Reset': False, 'LIT602': 700.4708988500334, 'T602Level': 700, 'WIN602': 3.6886764545564574, 'WOUT602': 0.6989501033163984, 'Waste602': 3924.86182247625, 'msg602': 'T602 draining: 3924.86182247625', 'T602Reset': False, 'P601': 2, 'P601T': 0, 'P601ON': 22527, 'FIT601O': 1.7863706506940544, 'P601Reset': False, 'P602': 3, 'P602T': 0, 'P602ON': 1397, 'FIT601': 0.06598875652551463, 'P602Reset': False, 'PLC6_COMM': '', 'Backwash': 3, 'BW': 2, 'BWCycle': 3, 'BWCount': 0, 'BWCycleCount': 22529, 'BWmsg': 'Sent run command to P601; T601 is above max permeate level (700\\nand T101 below HH (1200).', 'BWCycleError': 'None', 'BWCycleDuration': [0, 0, 0, 0, 0], 'HCF': 10000, 'TimeStamp': '03/12/2021 03:39:42', 'Terminate': False, 'HMI1': 'Control', 'HMI2': 'Display', 'HMI3': 'Display', 'HMI4': 'Display', 'HMI5': 'Display', 'Command': 'Run', 'HMIInControl': 1}"
    twin_message = "Extract! {'PLCStatus': {'PLC1': 'True', 'PLC2': 'True', 'PLC3': 'True' ,'PLC4': 'True', 'PLC5': 'True', 'PLC6': 'True'}, 'HMIButtonState': {'RunStop': 'Enable', 'Step': 'Enable', 'EmptyTanks': 'Enable', 'Speed': 'Enable'},'RWmsg': 'Plant in Standby', 'CDmsg': 'Plant in Standby', 'UFmsg': 'Plant in Standby', 'UVmsg': 'Plant in Standby', 'ROmsg': 'Plant in Standby',  'BWmsg': 'Plant in Standby', 'AICrit': {'ID': 'AICrit', 'Version': ['6.03', '15 October 2021'], 'Transition': 'False', 'Mode': 'Deployed', 'Predictions': {'LIT101': 3.461509363495967, 'LIT301': 813.0240844622004, 'LIT401': 554.3592796171828, 'FIT101': 0.0, 'FIT201': 2.471022, 'FIT301': 2.229382, 'FIT401': 0.23}, 'Anomaly': False, 'Invariants': []}, 'DAD': {'ID': 'DAD', 'Version': ['3.51', '29 Nov  2021'], 'Anomaly': False, 'Transition': 'False', 'Invariants': ['D6: LIT301 is Low, MV201 is open but none of P101 or P102 is running.', 'D10:1: Chatter on MV101; or closed when  LIT is between Low and High markers.'], 'Predictions': {'LIT101': 589.5202826388559, 'LIT301': 795.2085789709055, 'LIT401': 821.2465151714791}}, 'FIT504Mode': 'Manual', 'FIT201Mode': 'Manual', 'MV504Mode': 'Manual', 'PLC1_STATE': 1, 'PLC2_STATE': 1, 'PLC3_STATE': 1, 'PLC4_STATE': 1, 'PLC5_STATE': 1, 'PLC6_STATE': 1, 'SimStep': 0, 'PlantState': 'Running', 'Simspeed': 1, 'Exercise': 'CISS', 'WaterAvailable': 0, 'PlantReset': False, 'PlantTwinVizWin': 1, 'AttackObjectPLC5': {}, 'PLC1Reset': False, 'PLC2Reset': False, 'PLC3Reset': False, 'PLC4Reset': False, 'PLC5Reset': False, 'PLC6Reset': False, 'Tunnel': False, 'FIT101Markers': {'L': 2.5, 'H': 2.7}, 'FIT201Markers': {'L': 2.4, 'H': 2.6}, 'LIT101Markers': {'LL': 250, 'L': 500, 'H': 800, 'HH': 1200}, 'LIT201Markers': {'LL': 5, 'L': 5, 'H': 250, 'HH': 250}, 'LIT202Markers': {'LL': 2, 'L': 2, 'H': 25, 'HH': 25}, 'LIT203Markers': {'LL': 2, 'L': 5, 'H': 200, 'HH': 200}, 'AIT201Markers': {'L': 244, 'H': 272}, 'AIT202Markers': {'L': 6.95, 'H': 8.988273}, 'AIT203Markers': {'L': 300.8459, 'H': 567.46699}, 'FIT301Markers': {'L': 2.2, 'H': 2.4}, 'LIT301Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'AIT301Markers': {'L': 4.6, 'H': 6.2}, 'AIT302Markers': {'L': 379.7256, 'H': 480}, 'AIT303Markers': {'L': 7.49, 'H': 15.78}, 'DPIT301Markers': {'L': 0.04, 'H': 0.7}, 'DRN-TNKMarkers': {'L': 0, 'H': 1}, 'FIT401Markers': {'L': 0, 'H': 1.2490080000000001}, 'LIT401Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'LIT402Markers': {'LL': 25, 'L': 25, 'H': 250, 'HH': 250}, 'AIT401Markers': {'L': 22, 'H': 148.8561}, 'AIT402Markers': {'L': 153.7811, 'H': 235.7088}, 'PIT501Markers': {'L': 160, 'H': 300}, 'PIT502Markers': {'L': 250, 'H': 320}, 'PIT503Markers': {'L': 140, 'H': 210}, 'FIT501Markers': {'L': 1.1, 'H': 1.7}, 'FIT502Markers': {'L': 0.7, 'H': 1.1}, 'FIT503Markers': {'L': 0.35, 'H': 0.9}, 'FIT504Markers': {'L': 0.2, 'H': 0.4}, 'AIT501Markers': {'L': 5.6939, 'H': 6.7564}, 'AIT502Markers': {'L': 405.92, 'H': 494.001}, 'AIT503Markers': {'L': 91, 'H': 101}, 'AIT504Markers': {'L': 1.384, 'H': 73.7888}, 'ROD-BWTMarkers': {'L': 0, 'H': 1}, 'FIT601OMarkers': {'L': 1.6, 'H': 1.8}, 'FIT601Markers': {'L': 1.6, 'H': 1.8}, 'LIT601Markers': {'LL': 200, 'L': 300, 'H': 700, 'HH': 800}, 'LIT602Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 700}, 'MV101': 1, 'MV101Phy': 1, 'MV101T': 0, 'MV101ON': 0, 'MV101Reset': False, 'msgMV101': '', 'FIT101': 0.0, 'FIT101Phy': 0, 'FIT201': 0.0, 'FIT201Phy': 0, 'FlowStatus': [True, False], 'LIT101': 600.0, 'T101Level': 600, 'WIN101': 0, 'WOUT101': 0, 'PlantInflow': 0, 'T101IFA': 0, 'T101OFA': 0, 'P101': 1, 'P101Phy': 1, 'P101T': 0, 'P101ON': 0, 'P101Reset': False, 'P102': 1, 'P102Phy': 1, 'P102T': 0, 'P102ON': 0, 'P102Reset': False, 'RW': 0, 'PLC1_COMM': '', 'RWCount': 0, 'RWCycle': 1, 'RWCycleCount': 0, 'msgRecycle': '', 'RWCycleError': 'None', 'RWCycleDuration': [0, 0, 0, 0], 'PI101': 0, 'PI102': 0, 'Recycling': False, 'P101Mode': 'Manual', 'P102Mode': 'Manual', 'MV101Mode': 'Manual', 'LIT101Mode': 'Manual', 'FIT101Mode': 'Manual', 'MV201': 1, 'MV201Phy': 1, 'MV201T': 0, 'MV201ON': 0, 'MV201Reset': False, 'msgMV201': '', 'P201': 1, 'P201Phy': 1, 'P201T': 0, 'P201ON': 0, 'P201Reset': False, 'msgP201': '', 'P202': 1, 'P202Phy': 1, 'P202T': 0, 'P202ON': 0, 'P202Reset': False, 'msgP202': '', 'P203': 1, 'P203Phy': 1, 'P203T': 0, 'P203ON': 0, 'P203Reset': False, 'msgP203': '', 'P204': 1, 'P204Phy': 1, 'P204T': 0, 'P204ON': 0, 'P204Reset': False, 'msgP204': '', 'P205': 1, 'P205Phy': 1, 'P205T': 0, 'P205ON': 0, 'P205Reset': False, 'msgP205': '', 'P206': 1, 'P206Phy': 1, 'P206T': 0, 'P206ON': 0, 'P206Reset': False, 'msgP206': '', 'LIT201': 50.0, 'T201Level': 50, 'msgT201': 'T201initialized', 'LIT202': 5.0, 'T202Level': 5, 'msgT202': 'T202initialized', 'LIT203': 20.0, 'T203Level': 20, 'msgT203': 'T203initialized', 'CD': 0, 'CDCycle': 1, 'PLC2_COMM': '', 'AIT201': 243.0, 'AIT20Phy': 0, 'AIT202': 9.0, 'AIT202Phy': 0, 'AIT203': 300.0, 'AIT203Phy': 0, 'CDCount': 0, 'CDCycleCount': 0, 'CDCycleError': 'None', 'CDCycleDuration': [0, 0, 0, 0], 'ChemicalDosing': 1, 'FillCount201': 0, 'FillCount202': 0, 'FillCount203': 0, 'P201Mode': 'Manual', 'P202Mode': 'Manual', 'P203Mode': 'Manual', 'P204Mode': 'Manual', 'P205Mode': 'Manual', 'P206Mode': 'Manual', 'MV201Mode': 'Manual', 'LIT201Mode': 'Manual', 'LIT202Mode': 'Manual', 'LIT203Mode': 'Manual', 'AIT201Mode': 'Manual', 'AIT202Mode': 'Manual', 'AIT203Mode': 'Manual', 'MV301': 1, 'MV301Phy': 1, 'MV301T': 0, 'MV301ON': 0, 'MV301Reset': False, 'msgMV301': '', 'MV302': 1, 'MV302Phy': 1, 'MV302T': 0, 'MV302ON': 0, 'MV302Reset': False, 'MV303': 1, 'MV303Phy': 1, 'MV303T': 0, 'MV303ON': 0, 'MV303Reset': False, 'MV304': 1, 'MV304Phy': 1, 'MV304T': 0, 'MV304ON': 0, 'MV304Reset': False, 'LIT301': 798.0, 'T301Level': 798, 'WIN301': 0, 'WOUT301': 0, 'T301Inflow': 0, 'T301IFA': 0, 'T301OFA': 0, 'P301': 1, 'P301Phy': 1, 'P301T': 0, 'P301ON': 0, 'P301Reset': False, 'P302': 3, 'P302Phy': 3, 'P302T': 0, 'P302ON': 0, 'P302Reset': False, 'FIT301': 0.0, 'FIT301Phy': 0, 'FIT301ToT401': 0, 'DPIT301': 0.04161598, 'DPIT301Phy': 0.04161598, 'AIT301': 6.5, 'AIT301Phy': 6.5, 'AIT302': 378.0, 'AIT302Phy': 378, 'AIT303': 7.0, 'AIT303Phy': 7, 'RIOTrial': 1, 'RIOUFCount': 1, 'PLC3_COMM': '', 'UF': 0, 'UFCycle': 'Standby', 'UFCount': 0, 'UFCycleCount': 0, 'Ultrafiltration': 1, 'UFTimer': 0, 'UFCycleError': 'None', 'BC': 0, 'p201Start': False, 'DRN-TNK': [0, 0], 'waterAvailableStage3': -1, 'PI301': 0, 'PI302': 0, 'PI303': 0, 'PI304': 0, 'PI305': 0, 'P301Mode': 'Manual', 'P302Mode': 'Manual', 'MV301Mode': 'Manual', 'MV302Mode': 'Manual', 'MV303Mode': 'Manual', 'MV304Mode': 'Manual', 'FIT301Mode': 'Manual', 'LIT301Mode': 'Manual', 'AIT301Mode': 'Manual', 'AIT302Mode': 'Manual', 'AIT303Mode': 'Manual', 'DPIT301Mode': 'Manual', 'DRN-TNKMode': 'Auto', 'LIT401': 799.0, 'T401Level': 799, 'WIN401': 0, 'WOUT401': 0, 'T401Inflow': 0, 'T401IFA': 0, 'T401OFA': 0, 'LIT402': 250.0, 'T402Level': 250, 'FillCount402': 0, 'P401': 1, 'P401Phy': 1, 'P401T': 0, 'P401ON': 0, 'P401Reset': False, 'P402': 1, 'P402Phy': 1, 'P402T': 0, 'P402ON': 0, 'P403': 1, 'P403Phy': 1, 'P403T': 0, 'P403ON': 0, 'P403Reset': False, 'P404': 1, 'P404Phy': 1, 'P404T': 0, 'P404ON': 0, 'FIT401': 0.0, 'FIT401Phy': 0, 'FIT501P401': 0, 'msgP402': '', 'FIT501P402': 0, 'P402Reset': False, 'msgP403': '', 'msgT402': '', 'P404Reset': False, 'UV401': 1, 'UV401Phy': 1, 'AIT401': 148.8022, 'AIT40Phy': 148.8022, 'AIT402': 171.0587, 'AIT402Phy': 171.0587, 'AIT501': 6.8, 'AIT501Phy': 0, 'AIT502': 402.0, 'AIT502Phy': 0, 'AIT503': 88.0, 'AIT503Phy': 0, 'AIT504': 1.1, 'AIT504Phy': 0, 'UV401T': 0, 'UV401ON': 0, 'UV401Reset': False, 'PLC4_COMM': 'WP: SCADA. ', 'UV': 0, 'UVState': 1, 'UVCount': 0, 'UVCycle': 'Standby', 'UVTimer': 0, 'UVCycleCount': 0, 'UVCycleError': 'None', 'Dechlorination': 1, 'PI401': 0, 'PI402': 0, 'AttackableChemSensors': [''], 'ChemAttacks': [False], 'PressureAttacks': [False], 'P401Mode': 'Manual', 'P402Mode': 'Manual', 'P403Mode': 'Manual', 'P404Mode': 'Manual', 'UV401Mode': 'Manual', 'LIT401Mode': 'Manual', 'LIT402Mode': 'Manual', 'FIT401Mode': 'Manual', 'FIT401PhyMode': 'Auto', 'AIT401Mode': 'Manual', 'AIT401PhyMode': 'Auto', 'AIT402Mode': 'Manual', 'AIT402PhyMode': 'Auto', 'MV501': 1, 'MV501Phy': 1, 'MV501T': 0, 'MV501ON': 0, 'MV501Reset': False, 'msgMV501': '', 'MV502': 1, 'MV502Phy': 1, 'MV502T': 0, 'MV502ON': 0, 'MV502Reset': False, 'msgMV502': '', 'MV503': 1, 'MV503Phy': 1, 'MV503T': 0, 'MV503ON': 0, 'MV503Reset': False, 'msgMV503': '', 'MV504': 1, 'MV504Phy': 1, 'MV504T': 0, 'MV504ON': 0, 'MV504Reset': False, 'P501': 1, 'P501Phy': 1, 'P501Start': False, 'P501T': 0, 'P501ON': 0, 'P501Reset': False, 'FIT501': 0.0, 'FIT501Phy': 0, 'FIT502': 0.0, 'FIT502Phy': 0, 'FIT503': 0.0, 'FIT503Phy': 0, 'FIT504': 0.0, 'FIT504Phy': 0, 'PIT501': 10.34991, 'PIT501Phy': 10.34991, 'PIT502': 0.0, 'PIT502Phy': 0, 'PIT503': 4.614202, 'PIT503Phy': 4.614202, 'P502': 3, 'P502T': 0, 'P502ON': 0, 'P502Phy': 3, 'msgP502': '', 'RODrain': 0, 'RIOROCount': 1, 'PLC5_COMM': 'WP: SCADA. ', 'RO': 1, 'ROCycle': 'Standby', 'ROCount': 0, 'ROCycleCount': 0, 'ROTimer': 0, 'ROCycleError': 'None', 'StopP401': False, 'RunP401': False, 'ROD-BWT': [0, 0], 'FLV': False, 'ReverseOsmosis': 1, 'ShutdownCount': 0, 'AIT501High': False, 'AIT503High': False, 'PI501': 0, 'PI502': 0, 'chemSensorsUnderAttack': [''], 'FlowAttacks': [False], 'AttackDescription': '', 'P501Mode': 'Manual', 'P502Mode': 'Manual', 'MV501Mode': 'Manual', 'MV502Mode': 'Manual', 'MV503Mode': 'Manual', 'MV50Mode': 'Manual', 'FIT501Mode': 'Manual', 'FIT502Mode': 'Manual', 'PIT501Mode': 'Manual', 'PIT502Mode': 'Manual', 'PIT503Mode': 'Manual', 'FIT503Mode': 'Manual', 'FIT50Mode': 'Manual', 'AIT501Mode': 'Manual', 'AIT502Mode': 'Manual', 'AIT503Mode': 'Manual', 'AIT504Mode': 'Manual', 'ROD-BWTMode': 'Auto', 'LIT601': 600.0, 'T601Level': 600, 'WIN601': 0, 'WOUT601': 0, 'msg601': '', 'T601Reset': False, 'LIT602': 650.0, 'T602Level': 650, 'WIN602': 0, 'WOUT602': 0, 'Waste602': 0, 'msg602': '', 'T602Reset': False, 'P601': 1, 'P601Phy': 1, 'P601T': 0, 'P601ON': 0, 'P601Reset': False, 'P602': 1, 'P602Phy': 1, 'P602T': 0, 'P602ON': 0, 'P602Reset': False, 'FIT601O': 0.0, 'FIT601OPhy': 0, 'FIT601': 0.0, 'FIT601Phy': 0, 'FIT602Phy': 0, 'Backwash': 1, 'BW': 0, 'BWCycle': 1, 'BWCount': 0, 'BWCycleCount': 0, 'BWCycleError': 'None', 'BWCycleDuration': [0, 0, 0, 0, 0], 'PLC6_COMM': '', 'PI601': 0, 'PI602': 0, 'waterAvailableStage6': 21, 'P601Mode': 'Manual', 'P602Mode': 'Manual', 'LIT601Mode': 'Manual', 'LIT602Mode': 'Manual', 'FIT601Mode': 0, 'FIT601OMode': 0, 'HCF': 10000, 'LS201': 1, 'LS202': 1, 'LSL203': 1, 'LSLL203': 1, 'PSH301': 1, 'DPSH301': 1, 'LS401': 2, 'PSH501': 1, 'PSL501': 1, 'LSL601': 1, 'LSL602': 1, 'LSH601': 1, 'LSH602': 1, 'Version': 'Aug 26, 2021 Version CISS2021_03.06. ZMQ.OPC', 'TimeStamp': 'Mon Aug 30 2021 T06:41:45Z', 'Terminate': False, 'HMI1': 'Control', 'HMI2': 'Display', 'HMI3': 'Display', 'Command': 'Stop', 'HMIInControl': None}"
    # twin_message = "Extract! {'PLCStatus': {'PLC1': 'False', 'PLC2': 'True', 'PLC3': 'True' ,'PLC4': 'True', 'PLC5': 'True', 'PLC6': 'True'}, 'HMIButtonState': {'RunStop': 'Enable', 'Step': 'Enable', 'EmptyTanks': 'Enable', 'Speed': 'Enable'},'RWmsg': 'Plant in Standby', 'CDmsg': 'Plant in Standby', 'UFmsg': 'Plant in Standby', 'UVmsg': 'Plant in Standby', 'ROmsg': 'Plant in Standby',  'BWmsg': 'Plant in Standby', 'AICrit': {'ID': 'AICrit', 'Version': ['6.03', '15 October 2021'], 'Transition': 'False', 'Mode': 'Deployed', 'Predictions': {'LIT101': 3.461509363495967, 'LIT301': 813.0240844622004, 'LIT401': 554.3592796171828, 'FIT101': 0.0, 'FIT201': 2.471022, 'FIT301': 2.229382, 'FIT401': 0.23}, 'Anomaly': True, 'Invariants': ['R18: MV101 is CLOSED but LIT101 is below 500mm', 'R22: MV301 is OPEN but P602 is not running', 'R23: MV301 is OPEN but FIT601 is below 0.25 m3/hr', 'R24: Both MV301 and MV02 are OPEN', 'R25: Both MV301 and MV04 are OPEN', 'R29: MV303 is OPEN but FIT301 is above 1.7 m3/hr', 'R30: MV303 is OPEN but P301 is running', 'R42: P301 is running but MV301 is OPEN']}, 'DAD': {'ID': 'DAD', 'Version': ['3.51', '29 Nov  2021'], 'Anomaly': True, 'Transition': 'False', 'Invariants': ['D5_A: LIT101 is LowLow but P101 or P102 is running.', 'D7: LIT301 is high but P101 or P102 is running.', 'D43: Both MV301 (backwash valve) and MV304 (UF reject valve) are open.', 'D49: MV304 (UF reject valve) is open but MV301 is either closed and/or FIT601 is high.', 'D51: MV304 (UF reject valve) is open and orMV301 (UF backwash valve) is open, P601 is running, P602 is stopped.'], 'Predictions': {'LIT101': 589.5202826388559, 'LIT301': 795.2085789709055, 'LIT401': 821.2465151714791}}, 'FIT504Mode': 'Manual', 'FIT201Mode': 'Manual', 'MV504Mode': 'Manual', 'PLC1_STATE': 1, 'PLC2_STATE': 1, 'PLC3_STATE': 1, 'PLC4_STATE': 1, 'PLC5_STATE': 1, 'PLC6_STATE': 1, 'SimStep': 5, 'PlantState': 'Running', 'Simspeed': 1, 'Exercise': 'CISS', 'WaterAvailable': 0, 'PlantReset': False, 'PlantTwinVizWin': 1, 'AttackObjectPLC5': {}, 'PLC1Reset': False, 'PLC2Reset': False, 'PLC3Reset': False, 'PLC4Reset': False, 'PLC5Reset': False, 'PLC6Reset': False, 'Tunnel': False, 'FIT101Markers': {'L': 2.5, 'H': 2.7}, 'FIT201Markers': {'L': 2.4, 'H': 2.6}, 'LIT101Markers': {'LL': 250, 'L': 500, 'H': 800, 'HH': 1200}, 'LIT201Markers': {'LL': 5, 'L': 5, 'H': 250, 'HH': 250}, 'LIT202Markers': {'LL': 2, 'L': 2, 'H': 25, 'HH': 25}, 'LIT203Markers': {'LL': 2, 'L': 5, 'H': 200, 'HH': 200}, 'AIT201Markers': {'L': 244, 'H': 272}, 'AIT202Markers': {'L': 6.95, 'H': 8.988273}, 'AIT203Markers': {'L': 300.8459, 'H': 567.46699}, 'FIT301Markers': {'L': 2.2, 'H': 2.4}, 'LIT301Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'AIT301Markers': {'L': 4.6, 'H': 6.2}, 'AIT302Markers': {'L': 379.7256, 'H': 480}, 'AIT303Markers': {'L': 7.49, 'H': 15.78}, 'DPIT301Markers': {'L': 0.04, 'H': 0.7}, 'DRN-TNKMarkers': {'L': 0, 'H': 1}, 'FIT401Markers': {'L': 0, 'H': 1.2490080000000001}, 'LIT401Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'LIT402Markers': {'LL': 25, 'L': 25, 'H': 250, 'HH': 250}, 'AIT401Markers': {'L': 22, 'H': 148.8561}, 'AIT402Markers': {'L': 153.7811, 'H': 235.7088}, 'PIT501Markers': {'L': 160, 'H': 300}, 'PIT502Markers': {'L': 250, 'H': 320}, 'PIT503Markers': {'L': 140, 'H': 210}, 'FIT501Markers': {'L': 1.1, 'H': 1.7}, 'FIT502Markers': {'L': 0.7, 'H': 1.1}, 'FIT503Markers': {'L': 0.35, 'H': 0.9}, 'FIT504Markers': {'L': 0.2, 'H': 0.4}, 'AIT501Markers': {'L': 5.6939, 'H': 6.7564}, 'AIT502Markers': {'L': 405.92, 'H': 494.001}, 'AIT503Markers': {'L': 91, 'H': 101}, 'AIT504Markers': {'L': 1.384, 'H': 73.7888}, 'ROD-BWTMarkers': {'L': 0, 'H': 1}, 'FIT601OMarkers': {'L': 1.6, 'H': 1.8}, 'FIT601Markers': {'L': 1.6, 'H': 1.8}, 'LIT601Markers': {'LL': 200, 'L': 300, 'H': 700, 'HH': 800}, 'LIT602Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 700}, 'MV101': 1, 'MV101Phy': 1, 'MV101T': 0, 'MV101ON': 0, 'MV101Reset': False, 'msgMV101': '', 'FIT101': 0.0, 'FIT101Phy': 0, 'FIT201': 0.0, 'FIT201Phy': 0, 'FlowStatus': [True, False], 'LIT101': 600.0, 'T101Level': 600, 'WIN101': 0, 'WOUT101': 0, 'PlantInflow': 0, 'T101IFA': 0, 'T101OFA': 0, 'P101': 1, 'P101Phy': 1, 'P101T': 0, 'P101ON': 0, 'P101Reset': False, 'P102': 1, 'P102Phy': 1, 'P102T': 0, 'P102ON': 0, 'P102Reset': False, 'RW': 0, 'PLC1_COMM': '', 'RWCount': 0, 'RWCycle': 1, 'RWCycleCount': 0, 'msgRecycle': '', 'RWCycleError': 'None', 'RWCycleDuration': [0, 0, 0, 0], 'PI101': 0, 'PI102': 0, 'Recycling': False, 'P101Mode': 'Manual', 'P102Mode': 'Manual', 'MV101Mode': 'Manual', 'LIT101Mode': 'Manual', 'FIT101Mode': 'Manual', 'MV201': 1, 'MV201Phy': 1, 'MV201T': 0, 'MV201ON': 0, 'MV201Reset': False, 'msgMV201': '', 'P201': 1, 'P201Phy': 1, 'P201T': 0, 'P201ON': 0, 'P201Reset': False, 'msgP201': '', 'P202': 1, 'P202Phy': 1, 'P202T': 0, 'P202ON': 0, 'P202Reset': False, 'msgP202': '', 'P203': 1, 'P203Phy': 1, 'P203T': 0, 'P203ON': 0, 'P203Reset': False, 'msgP203': '', 'P204': 1, 'P204Phy': 1, 'P204T': 0, 'P204ON': 0, 'P204Reset': False, 'msgP204': '', 'P205': 1, 'P205Phy': 1, 'P205T': 0, 'P205ON': 0, 'P205Reset': False, 'msgP205': '', 'P206': 1, 'P206Phy': 1, 'P206T': 0, 'P206ON': 0, 'P206Reset': False, 'msgP206': '', 'LIT201': 50.0, 'T201Level': 50, 'msgT201': 'T201initialized', 'LIT202': 5.0, 'T202Level': 5, 'msgT202': 'T202initialized', 'LIT203': 20.0, 'T203Level': 20, 'msgT203': 'T203initialized', 'CD': 0, 'CDCycle': 1, 'PLC2_COMM': '', 'AIT201': 243.0, 'AIT20Phy': 0, 'AIT202': 9.0, 'AIT202Phy': 0, 'AIT203': 300.0, 'AIT203Phy': 0, 'CDCount': 0, 'CDCycleCount': 0, 'CDCycleError': 'None', 'CDCycleDuration': [0, 0, 0, 0], 'ChemicalDosing': 1, 'FillCount201': 0, 'FillCount202': 0, 'FillCount203': 0, 'P201Mode': 'Manual', 'P202Mode': 'Manual', 'P203Mode': 'Manual', 'P204Mode': 'Manual', 'P205Mode': 'Manual', 'P206Mode': 'Manual', 'MV201Mode': 'Manual', 'LIT201Mode': 'Manual', 'LIT202Mode': 'Manual', 'LIT203Mode': 'Manual', 'AIT201Mode': 'Manual', 'AIT202Mode': 'Manual', 'AIT203Mode': 'Manual', 'MV301': 1, 'MV301Phy': 1, 'MV301T': 0, 'MV301ON': 0, 'MV301Reset': False, 'msgMV301': '', 'MV302': 1, 'MV302Phy': 1, 'MV302T': 0, 'MV302ON': 0, 'MV302Reset': False, 'MV303': 1, 'MV303Phy': 1, 'MV303T': 0, 'MV303ON': 0, 'MV303Reset': False, 'MV304': 1, 'MV304Phy': 1, 'MV304T': 0, 'MV304ON': 0, 'MV304Reset': False, 'LIT301': 798.0, 'T301Level': 798, 'WIN301': 0, 'WOUT301': 0, 'T301Inflow': 0, 'T301IFA': 0, 'T301OFA': 0, 'P301': 1, 'P301Phy': 1, 'P301T': 0, 'P301ON': 0, 'P301Reset': False, 'P302': 3, 'P302Phy': 3, 'P302T': 0, 'P302ON': 0, 'P302Reset': False, 'FIT301': 0.0, 'FIT301Phy': 0, 'FIT301ToT401': 0, 'DPIT301': 0.04161598, 'DPIT301Phy': 0.04161598, 'AIT301': 6.5, 'AIT301Phy': 6.5, 'AIT302': 378.0, 'AIT302Phy': 378, 'AIT303': 7.0, 'AIT303Phy': 7, 'RIOTrial': 1, 'RIOUFCount': 1, 'PLC3_COMM': '', 'UF': 0, 'UFCycle': 'Standby', 'UFCount': 0, 'UFCycleCount': 0, 'Ultrafiltration': 1, 'UFTimer': 0, 'UFCycleError': 'None', 'BC': 0, 'p201Start': False, 'DRN-TNK': [0, 0], 'waterAvailableStage3': -1, 'PI301': 0, 'PI302': 0, 'PI303': 0, 'PI304': 0, 'PI305': 0, 'P301Mode': 'Manual', 'P302Mode': 'Manual', 'MV301Mode': 'Manual', 'MV302Mode': 'Manual', 'MV303Mode': 'Manual', 'MV304Mode': 'Manual', 'FIT301Mode': 'Manual', 'LIT301Mode': 'Manual', 'AIT301Mode': 'Manual', 'AIT302Mode': 'Manual', 'AIT303Mode': 'Manual', 'DPIT301Mode': 'Manual', 'DRN-TNKMode': 'Auto', 'LIT401': 799.0, 'T401Level': 799, 'WIN401': 0, 'WOUT401': 0, 'T401Inflow': 0, 'T401IFA': 0, 'T401OFA': 0, 'LIT402': 250.0, 'T402Level': 250, 'FillCount402': 0, 'P401': 1, 'P401Phy': 1, 'P401T': 0, 'P401ON': 0, 'P401Reset': False, 'P402': 1, 'P402Phy': 1, 'P402T': 0, 'P402ON': 0, 'P403': 1, 'P403Phy': 1, 'P403T': 0, 'P403ON': 0, 'P403Reset': False, 'P404': 1, 'P404Phy': 1, 'P404T': 0, 'P404ON': 0, 'FIT401': 0.0, 'FIT401Phy': 0, 'FIT501P401': 0, 'msgP402': '', 'FIT501P402': 0, 'P402Reset': False, 'msgP403': '', 'msgT402': '', 'P404Reset': False, 'UV401': 1, 'UV401Phy': 1, 'AIT401': 148.8022, 'AIT40Phy': 148.8022, 'AIT402': 171.0587, 'AIT402Phy': 171.0587, 'AIT501': 6.8, 'AIT501Phy': 0, 'AIT502': 402.0, 'AIT502Phy': 0, 'AIT503': 88.0, 'AIT503Phy': 0, 'AIT504': 1.1, 'AIT504Phy': 0, 'UV401T': 0, 'UV401ON': 0, 'UV401Reset': False, 'PLC4_COMM': 'WP: SCADA. ', 'UV': 0, 'UVState': 1, 'UVCount': 0, 'UVCycle': 'Standby', 'UVTimer': 0, 'UVCycleCount': 0, 'UVCycleError': 'None', 'Dechlorination': 1, 'PI401': 0, 'PI402': 0, 'AttackableChemSensors': [''], 'ChemAttacks': [False], 'PressureAttacks': [False], 'P401Mode': 'Manual', 'P402Mode': 'Manual', 'P403Mode': 'Manual', 'P404Mode': 'Manual', 'UV401Mode': 'Manual', 'LIT401Mode': 'Manual', 'LIT402Mode': 'Manual', 'FIT401Mode': 'Manual', 'FIT401PhyMode': 'Auto', 'AIT401Mode': 'Manual', 'AIT401PhyMode': 'Auto', 'AIT402Mode': 'Manual', 'AIT402PhyMode': 'Auto', 'MV501': 1, 'MV501Phy': 1, 'MV501T': 0, 'MV501ON': 0, 'MV501Reset': False, 'msgMV501': '', 'MV502': 1, 'MV502Phy': 1, 'MV502T': 0, 'MV502ON': 0, 'MV502Reset': False, 'msgMV502': '', 'MV503': 1, 'MV503Phy': 1, 'MV503T': 0, 'MV503ON': 0, 'MV503Reset': False, 'msgMV503': '', 'MV504': 1, 'MV504Phy': 1, 'MV504T': 0, 'MV504ON': 0, 'MV504Reset': False, 'P501': 1, 'P501Phy': 1, 'P501Start': False, 'P501T': 0, 'P501ON': 0, 'P501Reset': False, 'FIT501': 0.0, 'FIT501Phy': 0, 'FIT502': 0.0, 'FIT502Phy': 0, 'FIT503': 0.0, 'FIT503Phy': 0, 'FIT504': 0.0, 'FIT504Phy': 0, 'PIT501': 10.34991, 'PIT501Phy': 10.34991, 'PIT502': 0.0, 'PIT502Phy': 0, 'PIT503': 4.614202, 'PIT503Phy': 4.614202, 'P502': 3, 'P502T': 0, 'P502ON': 0, 'P502Phy': 3, 'msgP502': '', 'RODrain': 0, 'RIOROCount': 1, 'PLC5_COMM': 'WP: SCADA. ', 'RO': 1, 'ROCycle': 'Standby', 'ROCount': 0, 'ROCycleCount': 0, 'ROTimer': 0, 'ROCycleError': 'None', 'StopP401': False, 'RunP401': False, 'ROD-BWT': [0, 0], 'FLV': False, 'ReverseOsmosis': 1, 'ShutdownCount': 0, 'AIT501High': False, 'AIT503High': False, 'PI501': 0, 'PI502': 0, 'chemSensorsUnderAttack': [''], 'FlowAttacks': [False], 'AttackDescription': '', 'P501Mode': 'Manual', 'P502Mode': 'Manual', 'MV501Mode': 'Manual', 'MV502Mode': 'Manual', 'MV503Mode': 'Manual', 'MV50Mode': 'Manual', 'FIT501Mode': 'Manual', 'FIT502Mode': 'Manual', 'PIT501Mode': 'Manual', 'PIT502Mode': 'Manual', 'PIT503Mode': 'Manual', 'FIT503Mode': 'Manual', 'FIT50Mode': 'Manual', 'AIT501Mode': 'Manual', 'AIT502Mode': 'Manual', 'AIT503Mode': 'Manual', 'AIT504Mode': 'Manual', 'ROD-BWTMode': 'Auto', 'LIT601': 600.0, 'T601Level': 600, 'WIN601': 0, 'WOUT601': 0, 'msg601': '', 'T601Reset': False, 'LIT602': 650.0, 'T602Level': 650, 'WIN602': 0, 'WOUT602': 0, 'Waste602': 0, 'msg602': '', 'T602Reset': False, 'P601': 1, 'P601Phy': 1, 'P601T': 0, 'P601ON': 0, 'P601Reset': False, 'P602': 1, 'P602Phy': 1, 'P602T': 0, 'P602ON': 0, 'P602Reset': False, 'FIT601O': 0.0, 'FIT601OPhy': 0, 'FIT601': 0.0, 'FIT601Phy': 0, 'FIT602Phy': 0, 'Backwash': 1, 'BW': 0, 'BWCycle': 1, 'BWCount': 0, 'BWCycleCount': 0, 'BWCycleError': 'None', 'BWCycleDuration': [0, 0, 0, 0, 0], 'PLC6_COMM': '', 'PI601': 0, 'PI602': 0, 'waterAvailableStage6': 21, 'P601Mode': 'Manual', 'P602Mode': 'Manual', 'LIT601Mode': 'Manual', 'LIT602Mode': 'Manual', 'FIT601Mode': 0, 'FIT601OMode': 0, 'HCF': 10000, 'LS201': 1, 'LS202': 1, 'LSL203': 1, 'LSLL203': 1, 'PSH301': 1, 'DPSH301': 1, 'LS401': 2, 'PSH501': 1, 'PSL501': 1, 'LSL601': 1, 'LSL602': 1, 'LSH601': 1, 'LSH602': 1, 'Version': 'Aug 26, 2021 Version CISS2021_03.06. ZMQ.OPC', 'TimeStamp': 'Mon Aug 30 2021 T06:41:45Z', 'Terminate': False, 'HMI1': 'Control', 'HMI2': 'Display', 'HMI3': 'Display', 'Command': 'Run', 'HMIInControl': None}"
    # twin_message = "Extract! {'PLCStatus': {'PLC1': 'False', 'PLC2': 'True', 'PLC3': 'True' ,'PLC4': 'True', 'PLC5': 'True', 'PLC6': 'True'}, 'HMIButtonState': {'RunStop': 'Enable', 'Step': 'Enable', 'EmptyTanks': 'Enable', 'Speed': 'Enable'},'RWmsg': 'Plant in Standby', 'CDmsg': 'Plant in Standby', 'UFmsg': 'Plant in Standby', 'UVmsg': 'Plant in Standby', 'ROmsg': 'Plant in Standby',  'BWmsg': 'Plant in Standby', 'AICrit': {'ID': 'AICrit', 'Version': ['6.03', '15 October 2021'], 'Transition': 'False', 'Mode': 'Deployed', 'Predictions': {'LIT101': 3.461509363495967, 'LIT301': 813.0240844622004, 'LIT401': 554.3592796171828, 'FIT101': 0.0, 'FIT201': 2.471022, 'FIT301': 2.229382, 'FIT401': 0.23}, 'Anomaly': True, 'Invariants': ['R18: MV101 is CLOSED but LIT101 is below 500mm', 'R22: MV301 is OPEN but P602 is not running', 'R23: MV301 is OPEN but FIT601 is below 0.25 m3/hr', 'R24: Both MV301 and MV02 are OPEN', 'R25: Both MV301 and MV04 are OPEN', 'R29: MV303 is OPEN but FIT301 is above 1.7 m3/hr', 'R30: MV303 is OPEN but P301 is running', 'R42: P301 is running but MV301 is OPEN']}, 'DAD': {'ID': 'DAD', 'Version': ['3.51', '29 Nov  2021'], 'Anomaly': False, 'Transition': 'False', 'Invariants': [], 'Predictions': {'LIT101': 589.5202826388559, 'LIT301': 795.2085789709055, 'LIT401': 821.2465151714791}}, 'FIT504Mode': 'Manual', 'FIT201Mode': 'Manual', 'MV504Mode': 'Manual', 'PLC1_STATE': 1, 'PLC2_STATE': 1, 'PLC3_STATE': 1, 'PLC4_STATE': 1, 'PLC5_STATE': 1, 'PLC6_STATE': 1, 'SimStep': 6, 'PlantState': 'Running', 'Simspeed': 1, 'Exercise': 'CISS', 'WaterAvailable': 0, 'PlantReset': False, 'PlantTwinVizWin': 1, 'AttackObjectPLC5': {}, 'PLC1Reset': False, 'PLC2Reset': False, 'PLC3Reset': False, 'PLC4Reset': False, 'PLC5Reset': False, 'PLC6Reset': False, 'Tunnel': False, 'FIT101Markers': {'L': 2.5, 'H': 2.7}, 'FIT201Markers': {'L': 2.4, 'H': 2.6}, 'LIT101Markers': {'LL': 250, 'L': 500, 'H': 800, 'HH': 1200}, 'LIT201Markers': {'LL': 5, 'L': 5, 'H': 250, 'HH': 250}, 'LIT202Markers': {'LL': 2, 'L': 2, 'H': 25, 'HH': 25}, 'LIT203Markers': {'LL': 2, 'L': 5, 'H': 200, 'HH': 200}, 'AIT201Markers': {'L': 244, 'H': 272}, 'AIT202Markers': {'L': 6.95, 'H': 8.988273}, 'AIT203Markers': {'L': 300.8459, 'H': 567.46699}, 'FIT301Markers': {'L': 2.2, 'H': 2.4}, 'LIT301Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'AIT301Markers': {'L': 4.6, 'H': 6.2}, 'AIT302Markers': {'L': 379.7256, 'H': 480}, 'AIT303Markers': {'L': 7.49, 'H': 15.78}, 'DPIT301Markers': {'L': 0.04, 'H': 0.7}, 'DRN-TNKMarkers': {'L': 0, 'H': 1}, 'FIT401Markers': {'L': 0, 'H': 1.2490080000000001}, 'LIT401Markers': {'LL': 250, 'L': 800, 'H': 1000, 'HH': 1200}, 'LIT402Markers': {'LL': 25, 'L': 25, 'H': 250, 'HH': 250}, 'AIT401Markers': {'L': 22, 'H': 148.8561}, 'AIT402Markers': {'L': 153.7811, 'H': 235.7088}, 'PIT501Markers': {'L': 160, 'H': 300}, 'PIT502Markers': {'L': 250, 'H': 320}, 'PIT503Markers': {'L': 140, 'H': 210}, 'FIT501Markers': {'L': 1.1, 'H': 1.7}, 'FIT502Markers': {'L': 0.7, 'H': 1.1}, 'FIT503Markers': {'L': 0.35, 'H': 0.9}, 'FIT504Markers': {'L': 0.2, 'H': 0.4}, 'AIT501Markers': {'L': 5.6939, 'H': 6.7564}, 'AIT502Markers': {'L': 405.92, 'H': 494.001}, 'AIT503Markers': {'L': 91, 'H': 101}, 'AIT504Markers': {'L': 1.384, 'H': 73.7888}, 'ROD-BWTMarkers': {'L': 0, 'H': 1}, 'FIT601OMarkers': {'L': 1.6, 'H': 1.8}, 'FIT601Markers': {'L': 1.6, 'H': 1.8}, 'LIT601Markers': {'LL': 200, 'L': 300, 'H': 700, 'HH': 800}, 'LIT602Markers': {'LL': 200, 'L': 200, 'H': 700, 'HH': 700}, 'MV101': 1, 'MV101Phy': 1, 'MV101T': 0, 'MV101ON': 0, 'MV101Reset': False, 'msgMV101': '', 'FIT101': 0.0, 'FIT101Phy': 0, 'FIT201': 0.0, 'FIT201Phy': 0, 'FlowStatus': [True, False], 'LIT101': 600.0, 'T101Level': 600, 'WIN101': 0, 'WOUT101': 0, 'PlantInflow': 0, 'T101IFA': 0, 'T101OFA': 0, 'P101': 1, 'P101Phy': 1, 'P101T': 0, 'P101ON': 0, 'P101Reset': False, 'P102': 1, 'P102Phy': 1, 'P102T': 0, 'P102ON': 0, 'P102Reset': False, 'RW': 0, 'PLC1_COMM': '', 'RWCount': 0, 'RWCycle': 1, 'RWCycleCount': 0, 'msgRecycle': '', 'RWCycleError': 'None', 'RWCycleDuration': [0, 0, 0, 0], 'PI101': 0, 'PI102': 0, 'Recycling': False, 'P101Mode': 'Manual', 'P102Mode': 'Manual', 'MV101Mode': 'Manual', 'LIT101Mode': 'Manual', 'FIT101Mode': 'Manual', 'MV201': 1, 'MV201Phy': 1, 'MV201T': 0, 'MV201ON': 0, 'MV201Reset': False, 'msgMV201': '', 'P201': 1, 'P201Phy': 1, 'P201T': 0, 'P201ON': 0, 'P201Reset': False, 'msgP201': '', 'P202': 1, 'P202Phy': 1, 'P202T': 0, 'P202ON': 0, 'P202Reset': False, 'msgP202': '', 'P203': 1, 'P203Phy': 1, 'P203T': 0, 'P203ON': 0, 'P203Reset': False, 'msgP203': '', 'P204': 1, 'P204Phy': 1, 'P204T': 0, 'P204ON': 0, 'P204Reset': False, 'msgP204': '', 'P205': 1, 'P205Phy': 1, 'P205T': 0, 'P205ON': 0, 'P205Reset': False, 'msgP205': '', 'P206': 1, 'P206Phy': 1, 'P206T': 0, 'P206ON': 0, 'P206Reset': False, 'msgP206': '', 'LIT201': 50.0, 'T201Level': 50, 'msgT201': 'T201initialized', 'LIT202': 5.0, 'T202Level': 5, 'msgT202': 'T202initialized', 'LIT203': 20.0, 'T203Level': 20, 'msgT203': 'T203initialized', 'CD': 0, 'CDCycle': 1, 'PLC2_COMM': '', 'AIT201': 243.0, 'AIT20Phy': 0, 'AIT202': 9.0, 'AIT202Phy': 0, 'AIT203': 300.0, 'AIT203Phy': 0, 'CDCount': 0, 'CDCycleCount': 0, 'CDCycleError': 'None', 'CDCycleDuration': [0, 0, 0, 0], 'ChemicalDosing': 1, 'FillCount201': 0, 'FillCount202': 0, 'FillCount203': 0, 'P201Mode': 'Manual', 'P202Mode': 'Manual', 'P203Mode': 'Manual', 'P204Mode': 'Manual', 'P205Mode': 'Manual', 'P206Mode': 'Manual', 'MV201Mode': 'Manual', 'LIT201Mode': 'Manual', 'LIT202Mode': 'Manual', 'LIT203Mode': 'Manual', 'AIT201Mode': 'Manual', 'AIT202Mode': 'Manual', 'AIT203Mode': 'Manual', 'MV301': 1, 'MV301Phy': 1, 'MV301T': 0, 'MV301ON': 0, 'MV301Reset': False, 'msgMV301': '', 'MV302': 1, 'MV302Phy': 1, 'MV302T': 0, 'MV302ON': 0, 'MV302Reset': False, 'MV303': 1, 'MV303Phy': 1, 'MV303T': 0, 'MV303ON': 0, 'MV303Reset': False, 'MV304': 1, 'MV304Phy': 1, 'MV304T': 0, 'MV304ON': 0, 'MV304Reset': False, 'LIT301': 798.0, 'T301Level': 798, 'WIN301': 0, 'WOUT301': 0, 'T301Inflow': 0, 'T301IFA': 0, 'T301OFA': 0, 'P301': 1, 'P301Phy': 1, 'P301T': 0, 'P301ON': 0, 'P301Reset': False, 'P302': 3, 'P302Phy': 3, 'P302T': 0, 'P302ON': 0, 'P302Reset': False, 'FIT301': 0.0, 'FIT301Phy': 0, 'FIT301ToT401': 0, 'DPIT301': 0.04161598, 'DPIT301Phy': 0.04161598, 'AIT301': 6.5, 'AIT301Phy': 6.5, 'AIT302': 378.0, 'AIT302Phy': 378, 'AIT303': 7.0, 'AIT303Phy': 7, 'RIOTrial': 1, 'RIOUFCount': 1, 'PLC3_COMM': '', 'UF': 0, 'UFCycle': 'Standby', 'UFCount': 0, 'UFCycleCount': 0, 'Ultrafiltration': 1, 'UFTimer': 0, 'UFCycleError': 'None', 'BC': 0, 'p201Start': False, 'DRN-TNK': [0, 0], 'waterAvailableStage3': -1, 'PI301': 0, 'PI302': 0, 'PI303': 0, 'PI304': 0, 'PI305': 0, 'P301Mode': 'Manual', 'P302Mode': 'Manual', 'MV301Mode': 'Manual', 'MV302Mode': 'Manual', 'MV303Mode': 'Manual', 'MV304Mode': 'Manual', 'FIT301Mode': 'Manual', 'LIT301Mode': 'Manual', 'AIT301Mode': 'Manual', 'AIT302Mode': 'Manual', 'AIT303Mode': 'Manual', 'DPIT301Mode': 'Manual', 'DRN-TNKMode': 'Auto', 'LIT401': 799.0, 'T401Level': 799, 'WIN401': 0, 'WOUT401': 0, 'T401Inflow': 0, 'T401IFA': 0, 'T401OFA': 0, 'LIT402': 250.0, 'T402Level': 250, 'FillCount402': 0, 'P401': 1, 'P401Phy': 1, 'P401T': 0, 'P401ON': 0, 'P401Reset': False, 'P402': 1, 'P402Phy': 1, 'P402T': 0, 'P402ON': 0, 'P403': 1, 'P403Phy': 1, 'P403T': 0, 'P403ON': 0, 'P403Reset': False, 'P404': 1, 'P404Phy': 1, 'P404T': 0, 'P404ON': 0, 'FIT401': 0.0, 'FIT401Phy': 0, 'FIT501P401': 0, 'msgP402': '', 'FIT501P402': 0, 'P402Reset': False, 'msgP403': '', 'msgT402': '', 'P404Reset': False, 'UV401': 1, 'UV401Phy': 1, 'AIT401': 148.8022, 'AIT40Phy': 148.8022, 'AIT402': 171.0587, 'AIT402Phy': 171.0587, 'AIT501': 6.8, 'AIT501Phy': 0, 'AIT502': 402.0, 'AIT502Phy': 0, 'AIT503': 88.0, 'AIT503Phy': 0, 'AIT504': 1.1, 'AIT504Phy': 0, 'UV401T': 0, 'UV401ON': 0, 'UV401Reset': False, 'PLC4_COMM': 'WP: SCADA. ', 'UV': 0, 'UVState': 1, 'UVCount': 0, 'UVCycle': 'Standby', 'UVTimer': 0, 'UVCycleCount': 0, 'UVCycleError': 'None', 'Dechlorination': 1, 'PI401': 0, 'PI402': 0, 'AttackableChemSensors': [''], 'ChemAttacks': [False], 'PressureAttacks': [False], 'P401Mode': 'Manual', 'P402Mode': 'Manual', 'P403Mode': 'Manual', 'P404Mode': 'Manual', 'UV401Mode': 'Manual', 'LIT401Mode': 'Manual', 'LIT402Mode': 'Manual', 'FIT401Mode': 'Manual', 'FIT401PhyMode': 'Auto', 'AIT401Mode': 'Manual', 'AIT401PhyMode': 'Auto', 'AIT402Mode': 'Manual', 'AIT402PhyMode': 'Auto', 'MV501': 1, 'MV501Phy': 1, 'MV501T': 0, 'MV501ON': 0, 'MV501Reset': False, 'msgMV501': '', 'MV502': 1, 'MV502Phy': 1, 'MV502T': 0, 'MV502ON': 0, 'MV502Reset': False, 'msgMV502': '', 'MV503': 1, 'MV503Phy': 1, 'MV503T': 0, 'MV503ON': 0, 'MV503Reset': False, 'msgMV503': '', 'MV504': 1, 'MV504Phy': 1, 'MV504T': 0, 'MV504ON': 0, 'MV504Reset': False, 'P501': 1, 'P501Phy': 1, 'P501Start': False, 'P501T': 0, 'P501ON': 0, 'P501Reset': False, 'FIT501': 0.0, 'FIT501Phy': 0, 'FIT502': 0.0, 'FIT502Phy': 0, 'FIT503': 0.0, 'FIT503Phy': 0, 'FIT504': 0.0, 'FIT504Phy': 0, 'PIT501': 10.34991, 'PIT501Phy': 10.34991, 'PIT502': 0.0, 'PIT502Phy': 0, 'PIT503': 4.614202, 'PIT503Phy': 4.614202, 'P502': 3, 'P502T': 0, 'P502ON': 0, 'P502Phy': 3, 'msgP502': '', 'RODrain': 0, 'RIOROCount': 1, 'PLC5_COMM': 'WP: SCADA. ', 'RO': 1, 'ROCycle': 'Standby', 'ROCount': 0, 'ROCycleCount': 0, 'ROTimer': 0, 'ROCycleError': 'None', 'StopP401': False, 'RunP401': False, 'ROD-BWT': [0, 0], 'FLV': False, 'ReverseOsmosis': 1, 'ShutdownCount': 0, 'AIT501High': False, 'AIT503High': False, 'PI501': 0, 'PI502': 0, 'chemSensorsUnderAttack': [''], 'FlowAttacks': [False], 'AttackDescription': '', 'P501Mode': 'Manual', 'P502Mode': 'Manual', 'MV501Mode': 'Manual', 'MV502Mode': 'Manual', 'MV503Mode': 'Manual', 'MV50Mode': 'Manual', 'FIT501Mode': 'Manual', 'FIT502Mode': 'Manual', 'PIT501Mode': 'Manual', 'PIT502Mode': 'Manual', 'PIT503Mode': 'Manual', 'FIT503Mode': 'Manual', 'FIT50Mode': 'Manual', 'AIT501Mode': 'Manual', 'AIT502Mode': 'Manual', 'AIT503Mode': 'Manual', 'AIT504Mode': 'Manual', 'ROD-BWTMode': 'Auto', 'LIT601': 600.0, 'T601Level': 600, 'WIN601': 0, 'WOUT601': 0, 'msg601': '', 'T601Reset': False, 'LIT602': 650.0, 'T602Level': 650, 'WIN602': 0, 'WOUT602': 0, 'Waste602': 0, 'msg602': '', 'T602Reset': False, 'P601': 1, 'P601Phy': 1, 'P601T': 0, 'P601ON': 0, 'P601Reset': False, 'P602': 1, 'P602Phy': 1, 'P602T': 0, 'P602ON': 0, 'P602Reset': False, 'FIT601O': 0.0, 'FIT601OPhy': 0, 'FIT601': 0.0, 'FIT601Phy': 0, 'FIT602Phy': 0, 'Backwash': 1, 'BW': 0, 'BWCycle': 1, 'BWCount': 0, 'BWCycleCount': 0, 'BWCycleError': 'None', 'BWCycleDuration': [0, 0, 0, 0, 0], 'PLC6_COMM': '', 'PI601': 0, 'PI602': 0, 'waterAvailableStage6': 21, 'P601Mode': 'Manual', 'P602Mode': 'Manual', 'LIT601Mode': 'Manual', 'LIT602Mode': 'Manual', 'FIT601Mode': 0, 'FIT601OMode': 0, 'HCF': 10000, 'LS201': 1, 'LS202': 1, 'LSL203': 1, 'LSLL203': 1, 'PSH301': 1, 'DPSH301': 1, 'LS401': 2, 'PSH501': 1, 'PSL501': 1, 'LSL601': 1, 'LSL602': 1, 'LSH601': 1, 'LSH602': 1, 'Version': 'Aug 26, 2021 Version CISS2021_03.06. ZMQ.OPC', 'TimeStamp': 'Mon Aug 30 2021 T06:41:45Z', 'Terminate': False, 'HMI1': 'Control', 'HMI2': 'Display', 'HMI3': 'Display', 'Command': 'Run', 'HMIInControl': None}"

    # Protocol loop goes here. It has four steps.    
    while True:
        if(steps == 1):
            print("Starting bridge...")
            # print(twin_message)

        # Step 1: Receive plant state from the Twin - remote or local
        try:
            twin_message = TWIN_SUBSCRIBE_SOCKET.recv()
            # print(twin_message)
        except:
            print("Error: Nothing received from twin, step: " + str(steps))
        
        
        # steps = steps + 1
        twin_state_dictionary =  eval(twin_message[len(TWIN_SUBSCRIBE_TOPIC):])

        #Step 1.5
        PythonUnityBridge_message = processPlantState(twin_state_dictionary)

        # Step 2: Publish HMI-friendly state to the Unity HMI
        try:
            PythonUnityBridge_PUBLISH_SOCKET.send_string(PythonUnityBridge_message)
            print("Sent message "+ str(twin_state_dictionary["SimStep"]))
            # print(PythonUnityBridge_message)
            steps = twin_state_dictionary["SimStep"]
        except:
            print("Error: Stuck at publishing to HMI at step " + str(steps))

if __name__ == "__main__":
    main()
