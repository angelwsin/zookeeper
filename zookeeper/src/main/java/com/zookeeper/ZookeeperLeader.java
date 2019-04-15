package com.zookeeper;

import org.apache.zookeeper.server.quorum.QuorumPeerMain;

public class ZookeeperLeader {


    public static void main(String[] args) {
        QuorumPeerMain.main(new String[]{"/Users/mac/dev/git/zookeeper/zookeeper/src/main/resoures/zoo-1.cfg"});
    }
}
