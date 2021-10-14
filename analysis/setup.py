import os
import csv
import numpy as np
import pandas as pd
import matplotlib
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
from scipy.special import factorial


def load_data(folder_path):
    files_list = os.listdir(folder_path)

    df = set_dataframe(folder_path, files_list[0])
    
    for i in range(len(files_list)-1):
        df = pd.concat([df, set_dataframe(folder_path, files_list[i+1])])
    return df

def string_to_bool(value):
    if value == 'false':
        return False
    else:
        return True

def set_dataframe(path, filename):
    df = pd.read_csv(path+filename)

    # AN_B_NH_NS_NR_MS_NM_H_TS.csv
    parameters = filename.replace('.csv','').split('_')
    
    agent_number     = parameters[0]
    benchmark        = parameters[1]
    number_hosts     = parameters[2]
    number_senders   = parameters[3]
    number_receivers = parameters[4]
    message_size     = parameters[5]
    number_messages  = parameters[6]
    henrique         = parameters[7]
    timestamp        = parameters[8]
    
    df['Benchmark']       = benchmark
    df['NumberHosts']     = number_hosts
    df['NumberSenders']   = number_senders
    df['NumberReceivers'] = number_receivers
    df['MessageSize']     = message_size
    df['NumberMessages']  = number_messages
    df['Henrique']        = string_to_bool(henrique)
    
    df['RTT'] = pd.to_numeric(df['RTT'], downcast="integer")
    df['Benchmark'] = pd.to_numeric(df['Benchmark'], downcast="integer")
    df['NumberHosts'] = pd.to_numeric(df['NumberHosts'], downcast="integer")
    df['NumberSenders'] = pd.to_numeric(df['NumberSenders'], downcast="integer")
    df['NumberReceivers'] = pd.to_numeric(df['NumberReceivers'], downcast="integer")
    df['MessageSize'] = pd.to_numeric(df['MessageSize'], downcast="integer")
    df['NumberMessages'] = pd.to_numeric(df['NumberMessages'], downcast="integer")
    
    return df

def plot_results(df, title, x_label, x_column, yerror=False, legentOut=False, logScale=False):
    fig, ax = plt.subplots(figsize=(10, 6))
    ax.set_ylabel('Média de RTT (ms)\n', fontsize=16)
    ax.set_xlabel('\n'+x_label, fontsize=16)
    
    if yerror:
        plt.errorbar(df[x_column], df['RTT_mean'], fmt='--s')
    else:
        plt.errorbar(df[x_column], df['RTT_mean'], fmt='--s')
        
    if logScale:
        ax.set_xscale('log')
    #if legentOut:
    #    ax.legend(legend, loc='upper right', bbox_to_anchor=(1.36 , 1.015), fontsize=12)
    #else:
    #    ax.legend(legend, fontsize=12)
    fig.tight_layout()
    if logScale:
        plt.yscale = "log"
    plt.show()
    
def plot_results2(dfs, x_label, legend, yerror=False, legentOut=False, logScale=False):

    fig, ax = plt.subplots(figsize=(10, 6))
    ax.set_ylabel('Média de RTT (ms)\n', fontsize=16)
    ax.set_xlabel('\n'+x_label, fontsize=16)
    
    for df in dfs:
        if yerror:
            plt.errorbar(df.index, df['RTT_mean'], fmt='--s')
        else:
            plt.errorbar(df.index, df['RTT_mean'], fmt='--s')
        
    if logScale:
        ax.set_xscale('log')
    if legentOut:
        ax.legend(legend, loc='upper right', bbox_to_anchor=(1.36 , 1.015), fontsize=12)
    else:
        ax.legend(legend, fontsize=12)
    fig.tight_layout()
    if logScale:
        plt.yscale = "log"
    plt.show() 
        
    