/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  NativeModules,
} from 'react-native';

import {Colors, Header} from 'react-native/Libraries/NewAppScreen';
import Pressable from 'react-native/Libraries/Components/Pressable/Pressable';

const {SecureSensitiveInfo} = NativeModules;

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Pressable
            onPress={() => {
              console.log('invoking native module method...');
              SecureSensitiveInfo.secureInfo('secretKey', 'SECRET_VALUE');
            }}
            style={styles.btn}>
            <Text style={styles.btnText}>Set secured info</Text>
          </Pressable>
          <Pressable
            onPress={() => {
              console.log('invoking native module method...');
              SecureSensitiveInfo.getInfo('secretKey');
            }}
            style={[styles.btn, {backgroundColor: 'green'}]}>
            <Text style={styles.btnText}>Get secured info</Text>
          </Pressable>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },

  btn: {
    backgroundColor: 'red',
    padding: 10,
    borderRadius: 10,
    width: '60%',
    marginVertical: 10,
    alignSelf: 'center',
  },

  btnText: {
    color: '#fff',
    fontWeight: '700',
  },
});

export default App;
